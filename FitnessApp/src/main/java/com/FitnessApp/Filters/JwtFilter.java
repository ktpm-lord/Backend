package com.FitnessApp.Filters;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.FitnessApp.Config.WebSecurityConfig;
import com.FitnessApp.DTO.DataClass.ResponseObject;
import com.FitnessApp.Utils.JwtTokenUtils;
import jakarta.servlet.ServletException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.ErrorResponse;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.FitnessApp.Security.Model.UserDetailServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtTokenUtils tokenProvider;

	private final UserDetailServiceImp customUserDetailsService;

	private final UserDetailServiceImp userDetailImp;

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws IOException {
		try {
			String jwt = getJwtFromRequest(request);

			if (Arrays.stream(WebSecurityConfig.whiteListedRoutes)
					.anyMatch(route -> antPathMatcher.match(route, request.getServletPath())) ||
					Objects.isNull(jwt)) {
				filterChain.doFilter(request, response);
				return;
			}


            if (tokenProvider.validateToken(jwt)) {
				String userId = tokenProvider.getUsernameFromToken(jwt);
				UserDetails userDetails = userDetailImp.loadUserByUsername(userId);

				if (userDetails != null) {
					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

//					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			handleErrorResponse(response,e);
		}
	}

	private void handleErrorResponse(HttpServletResponse response,Exception e) throws IOException {
		ResponseObject errorResponse = new ResponseObject(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null);

		response.resetBuffer();
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getOutputStream().print(new ObjectMapper().writeValueAsString(errorResponse));
		response.flushBuffer();
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		// Kiểm tra xem header Authorization có chứa thông tin jwt không
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
