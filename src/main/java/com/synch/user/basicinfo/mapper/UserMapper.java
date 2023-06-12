package com.synch.user.basicinfo.mapper;

import com.synch.user.basicinfo.dto.UserDetailsDTO;
import com.synch.user.basicinfo.model.User;

public class UserMapper {

    public static User toEntity(UserDetailsDTO userDetailsDTO){
        User user= new User();
        user.setUserName(userDetailsDTO.getUserName());
        user.setPassword(userDetailsDTO.getPassword());
        return user;
    }

    public static UserDetailsDTO toAPI(User userObj, String file){
        UserDetailsDTO user= new UserDetailsDTO();
        user.setUserName(userObj.getUserName());
        user.setFilePath(file);
        return user;
    }

}
