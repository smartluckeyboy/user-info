package com.synch.user.basicinfo.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
@Order(1)
@Log4j2
public class AuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserAuthService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Basic ")) {
            String userCred = requestTokenHeader.split(" ")[1];
            Base64.Decoder decoder = Base64.getDecoder();
            // Decoding string
            userCred = new String(decoder.decode(userCred));
            log.debug("Decoded string: "+userCred);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userCred.split(":",2)[0], userCred.split(":",2)[1]));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }
}
