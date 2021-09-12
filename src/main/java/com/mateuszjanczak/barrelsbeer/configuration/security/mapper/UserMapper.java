package com.mateuszjanczak.barrelsbeer.configuration.security.mapper;

import com.mateuszjanczak.barrelsbeer.configuration.security.dto.UserResponse;
import com.mateuszjanczak.barrelsbeer.configuration.security.entity.User;

public class UserMapper {

    public static UserResponse userToResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}
