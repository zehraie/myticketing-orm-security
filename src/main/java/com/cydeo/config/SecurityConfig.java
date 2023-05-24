package com.cydeo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration // I want to use something  from Spring , I need some class, interface so build this classsc-1067
public class SecurityConfig {
    @Bean // what spring provides to me?
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
   // I am creating my user usning Spring classes
       // UserDetails user = new User()  for one user, but need more than one user, this is how we create users MANUALLY
        List<UserDetails> userList = new ArrayList<>();
        userList.add(
                new User("mike",encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
        userList.add(
                new User("ozzy",encoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER"))));

                return new InMemoryUserDetailsManager(userList);
    }
}
// careful User class NOT ENTITY this User class created by spring
// main UserDetailsService this bussiness logic provide by public interface UserDetailsService
// and return UserDetails
//what is the mekanism bring the user into the UI , it is loadUserByUsername interfaace under the UserDetailService interdace class
// Spring created UserDetailService interface class and execude loadUserByName interface which is pasing the object into the UI
// got to UserDetailService interface and click the UserDetails class
//ROLE_ADMIN is name convention by spring