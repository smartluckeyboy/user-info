package com.synch.user.basicinfo.service;

import com.synch.user.basicinfo.dto.UserDetailsDTO;
import com.synch.user.basicinfo.exceptions.UserNotFoundException;
import com.synch.user.basicinfo.model.User;
import com.synch.user.basicinfo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.synch.user.basicinfo.mapper.UserMapper.toEntity;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public User register(UserDetailsDTO userDetailsDTO){
        userDetailsDTO.setPassword(encoder.encode(userDetailsDTO.getPassword()));
      return   Optional.ofNullable(userDetailsDTO)
                .map(u-> toEntity(userDetailsDTO))
                .map(u-> userRepository.save(u))
              .orElseThrow(()-> new RuntimeException("Error occurred during saving user credentials "));

    }

    public User getUserDetails(long id){
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
