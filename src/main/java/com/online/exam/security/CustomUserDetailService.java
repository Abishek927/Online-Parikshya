package com.online.exam.security;

import com.online.exam.dto.UserDto;
import com.online.exam.model.User;
import com.online.exam.repo.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findByUserName(username);
        if(user==null){
            throw new UsernameNotFoundException("User not found"+username);
        }
        UserDto userDto=this.modelMapper.map(user,UserDto.class);
        UserPrincipal userPrincipal=new UserPrincipal(userDto);

        return userPrincipal;
    }
}
