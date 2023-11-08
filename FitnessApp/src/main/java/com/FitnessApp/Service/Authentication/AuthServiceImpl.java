package com.FitnessApp.Service.Authentication;

import com.FitnessApp.DTO.*;
import com.FitnessApp.DTO.User.UserDTO;
import com.FitnessApp.Exceptions.NotFoundException;
import com.FitnessApp.Mapper.UserMapper;
import com.FitnessApp.Model.User;
import com.FitnessApp.Model.UserProfile;
import com.FitnessApp.Repository.UserProfileRepository;
import com.FitnessApp.Repository.UserRepository;
import com.FitnessApp.Utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Primary
@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService{
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private AuthenticationManager authenticationManager;
private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserProfileRepository userProfileRepository;

    @Override
    public AuthResponse login(AuthRequest request) {
        List<User> findByUsername = userRepository.findByUsername(request.username());

        if (findByUsername.isEmpty()){
            throw new NotFoundException("Can not find user have this username");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.username(),request.password()));

        String jwt = jwtTokenUtils.generateToken(request.username());
        String freshToken = jwtTokenUtils.generateTokenRefresh(request.username());

        final User user = findByUsername.get(0);

        user.setRefreshToken(freshToken);

        final User currentUser  = userRepository.save(user);
        final UserDTO userDTO = userMapper.userDTO(currentUser);
        return new AuthResponse(jwt,freshToken,userDTO);
    }


    @Override
    public ResponseEntity<?> register(RegistrationRequest request) {
        try {
            List<User> userExits = userRepository.findByUsername(request.getUsername());

            if (!userExits.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        ResponseObject.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .message("User name existed")
                                .data(null)
                                .build());
            }


            String jwt = jwtTokenUtils.generateToken(request.getUsername());
            String freshToken = jwtTokenUtils.generateTokenRefresh(request.getUsername());

            User newUser = new User(
                null,
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()) ,
                freshToken,
                null
            );

            final User savedUser = userRepository.save(newUser);
            UserProfile userProfile = UserProfile
                    .builder()
                    .user(savedUser)
                    .build();

            final UserProfile saveUserProfile = userProfileRepository.save(userProfile);
            savedUser.setUserProfile(saveUserProfile);
            final User finalUser = userRepository.save(savedUser);
            final UserDTO userDto = userMapper.userDTO(finalUser);

            return ResponseEntity.ok(
                    new AuthResponse(jwt,freshToken,userDto)
            );
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(new ResponseObject("INTERNAL_SERVER_ERROR","Failed: Can not register user \n" + e.getMessage(),null));
        }
    }

    @Override
    public ResponseEntity<?> refreshToken(String token) {
        try {

            if (token == null || !token.startsWith("Bearer ")) {
                throw new Exception("Invalid refresh token");
            }
            final String refreshToken = token.substring("Bearer ".length());

            jwtTokenUtils.validateToken(refreshToken);

            final String username = jwtTokenUtils.getUsernameFromToken(refreshToken);
            List<User> user = userRepository.findByUsername(username);

            if (user.isEmpty()){
                throw new NotFoundException("Can not found corresponding user");
            }

            final String jwt = jwtTokenUtils.generateToken(user.get(0).getUsername());

            return ResponseEntity.ok(new TokenResponse(jwt,refreshToken));

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(
                     ResponseObject.builder()
                             .message(e.getMessage())
                             .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                             .build()
            );
        }
    }

    @Override
    public ResponseEntity<?> token(UserDetails request) {
        try {
            List<User> findUser = userRepository.findByUsername(request.getUsername());
            if (findUser.isEmpty()){
                throw new NotFoundException("Can not found corresponding user");
            }

            String accessToken = jwtTokenUtils.generateToken(request.getUsername());
            String refreshToken = jwtTokenUtils.generateTokenRefresh(request.getUsername());

            return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(
                    new ResponseObject(HttpStatus.BAD_GATEWAY.getReasonPhrase(), e.getMessage(),null)
            );
        }
    }
}