package com.example.BlogDemo.service;

import com.example.BlogDemo.Dao.UserRepository;
import com.example.BlogDemo.config.MyUserDetails;
import com.example.BlogDemo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            return new MyUserDetails(optionalUser.get());
        }
        else {
            throw new UsernameNotFoundException("User not exist");
        }
    }
}
