package com.sky.whatsapp.service;

import com.sky.whatsapp.config.TokenProvider;
import com.sky.whatsapp.exception.UserException;
import com.sky.whatsapp.model.User;
import com.sky.whatsapp.repository.UserRepository;
import com.sky.whatsapp.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService{
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public User findUserById(Integer id) throws UserException {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isPresent()){
            return opt.get();
        }
        throw new UserException("User Not found with id "+id);
    }

    @Override
    public User findUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);
        if (email==null){
           throw new BadCredentialsException("received invalid token -- ");
        }
        User user = userRepository.findUserByEmail(email);
        if (user == null){
            throw new UserException("user not found with email "+ email);
        }
        return user;
    }

    @Override
    public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
        User user = findUserById(userId);
        if (req.getFullName() != null){
            user.setFullName(req.getFullName());
        }
        if (req.getProfilePicture() != null){
            user.setProfilePicture(req.getProfilePicture());
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {
        List<User> users = userRepository.searchUser(query);
        return users;
    }
}
