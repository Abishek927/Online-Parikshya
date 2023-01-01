package com.online.exam.config;

import com.online.exam.exception.UserNotFoundException;
import com.online.exam.model.User;
import com.online.exam.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User resultUser = this.userRepo.findByUserName(username);
        CustomUserDetails customUserDetails;
        if (resultUser == null) {
            throw new UserNotFoundException("user not found with", username);
        } else {
            customUserDetails = new CustomUserDetails(resultUser);
        }


        return customUserDetails;
    }
}
