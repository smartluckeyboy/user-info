package com.synch.user.basicinfo.repository;

import com.synch.user.basicinfo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {

    public User findByUserName(String userName);
}
