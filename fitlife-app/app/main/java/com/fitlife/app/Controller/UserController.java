package com.fitlife.app.Controller;

import com.cloudinary.api.exceptions.BadRequest;
import com.fitlife.app.DTO.DataClass.User.UserDTO;
import com.fitlife.app.DTO.Request.AddActivitiesLogRequest;
import com.fitlife.app.DTO.Request.ChangePasswordRequest;
import com.fitlife.app.Exceptions.AppException.BadRequestException;
import com.fitlife.app.Security.Model.CurrentUser;
import com.fitlife.app.Security.Model.FitLifeUserDetail;
import com.fitlife.app.Service.User.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final IUserService uService;

    @GetMapping("/{id}")
    ResponseEntity<?> getUser(@PathVariable long id){
        return ResponseEntity.ok().body(uService.getUserById(id));
    }

    @GetMapping("/profile/{id}")
    ResponseEntity<?> getUserProfile(@PathVariable long id){
        return ResponseEntity.ok(uService.getUserProfile(id));
    }

    @GetMapping("/name/{username}")
    ResponseEntity<?> getUserByUserName(@PathVariable String username){
        return ResponseEntity.ok(uService.getUserByUsername(username));
    }
    @GetMapping
    ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok(uService.getAllUser());
    }

    @GetMapping("/me")
    public ResponseEntity<?> profile(@CurrentUser FitLifeUserDetail flUserDetail) {
        UserDTO user = uService.getUserById(flUserDetail.getId());
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@CurrentUser FitLifeUserDetail ctx, @Valid @RequestBody ChangePasswordRequest request){
        return ResponseEntity.ok(uService.changePassword(ctx.getId(),request));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@CurrentUser FitLifeUserDetail ctx, @Valid @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(uService.updateUserProfile(ctx.getId(),userDTO));
    }

    @PutMapping("/exercise-favorite/{id}")
    public ResponseEntity<?> addFavoriteExercise(@CurrentUser FitLifeUserDetail ctx, @PathVariable Long id) throws BadRequestException {
        final var responseObject = uService.addFavoriteExercise(ctx.getId(),id);
        return new ResponseEntity<>(responseObject.message(), HttpStatusCode.valueOf(responseObject.status()));
    }

    @PutMapping("/news-favorite/{id}")
    public ResponseEntity<?> addFavoriteNews(@CurrentUser FitLifeUserDetail ctx, @PathVariable  Long id) throws BadRequestException{
        final var responseObject = uService.addFavoriteNews(ctx.getId(),id);
        return new ResponseEntity<>(responseObject.message(), HttpStatusCode.valueOf(responseObject.status()));
    }

    @PutMapping("/change-current-plan/{id}")
    public ResponseEntity<?> changeCurrentPlan(@CurrentUser FitLifeUserDetail ctx, @PathVariable Long id)  {
        return  ResponseEntity.ok(uService.changeCurrentPlan(ctx.getId(),id));
    }
    @GetMapping("/current-plan")
    public ResponseEntity<?> getCurrentPlan(@CurrentUser FitLifeUserDetail ctx) {
        return ResponseEntity.ok(uService.getCurrentPlan(ctx.getId()));
    }


    @PostMapping("/activity-log")
    public ResponseEntity<?> addActivityLog(
            @CurrentUser FitLifeUserDetail ctx,
            @Valid @RequestBody AddActivitiesLogRequest request
    ) throws BadRequestException {
        final var responseObject = uService.addActivityLog(ctx.getId(),request);
        return new ResponseEntity<>(responseObject.message(), HttpStatusCode.valueOf(responseObject.status()));
    }
}
