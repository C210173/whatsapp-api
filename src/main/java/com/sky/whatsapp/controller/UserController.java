package com.sky.whatsapp.controller;

import com.sky.whatsapp.exception.UserException;
import com.sky.whatsapp.model.User;
import com.sky.whatsapp.request.UpdateUserRequest;
import com.sky.whatsapp.response.ApiResponse;
import com.sky.whatsapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserProfile(token);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String query){
            List<User> users = userService.searchUser(query);
            return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest req, @RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserProfile(token);

        userService.updateUser(user.getId(), req);
        ApiResponse res = new ApiResponse("user updated successfully", true);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
