package com.sky.whatsapp.service;

import com.sky.whatsapp.exception.UserException;
import com.sky.whatsapp.model.User;
import com.sky.whatsapp.request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    User findUserById(Integer id) throws UserException;
    User findUserProfile(String jwt) throws UserException;
    User updateUser(Integer userId, UpdateUserRequest req) throws UserException;
    List<User> searchUser(String query);

}
