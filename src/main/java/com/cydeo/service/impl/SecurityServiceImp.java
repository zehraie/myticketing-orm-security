package com.cydeo.service.impl;


import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrincipal;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImp implements SecurityService {

    private final UserRepository userRepository;

    public SecurityServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user==null){
            throw new UsernameNotFoundException("this user does not exists");
        }
        return new UserPrincipal(user);
    }
    // i got the  user from db now I will give it to UI by means of LoadByUser
    //mapper ile ceviriyourm with userPrincipal
}
