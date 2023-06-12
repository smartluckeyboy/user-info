package com.synch.user.basicinfo.service;

import com.synch.user.basicinfo.dto.UserDetailsDTO;
import com.synch.user.basicinfo.model.User;
import com.synch.user.basicinfo.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    UserDetailsDTO userDetailsMock;

    @Mock
    User userMock;

    @Before("")
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerTest_Success()
    {

        UserDetailsDTO userDetailsDTO=new UserDetailsDTO();
        userDetailsDTO.setUserName("test");
        userDetailsDTO.setPassword("test");
        User user=new User();
        user.setUserName("test");
        user.setPassword("12345667ASDFERfvfvf");
        when(userDetailsMock.getUserName()).thenReturn("test");
        when(userDetailsMock.getPassword()).thenReturn("test");
        when(encoder.encode(userDetailsMock.getPassword())).thenReturn("test");
        lenient().when(userRepository.save(userMock)).thenReturn(userMock);

        when(userService.register(userDetailsMock)).thenReturn(user);

        //test
        User userDetails= userService.register(userDetailsMock);
        assertEquals("test", userDetails.getUserName());
    }
    
}
