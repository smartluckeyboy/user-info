package com.synch.user.basicinfo.auth;

import com.synch.user.basicinfo.repository.UserRepository;
import com.synch.user.basicinfo.util.StaticClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Log4j2
public class UserAuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
            com.synch.user.basicinfo.model.User user = userRepository.findByUserName(principal);
            if (user!=null) {
                StaticClass.getThreadLocalUserId().set(user.getId());
                return new User(user.getUserName(),user.getPassword(), new ArrayList<>());

            } else {
                log.error("user not found : ");
                throw new UsernameNotFoundException("User not found !!");
            }
        }
}
