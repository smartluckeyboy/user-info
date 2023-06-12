package com.synch.user.basicinfo.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDetailsDTO {

    private String userName;
    private String password;
    private String filePath;

}
