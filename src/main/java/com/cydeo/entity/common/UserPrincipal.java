package com.cydeo.entity.common;

import com.cydeo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/*
In the given code snippet, the constructor of the UserPrincipal class takes a User object as a parameter. This User object represents the user entity, which contains information about the user such as their username, password, role, and account status.
 */

public class UserPrincipal implements UserDetails {
    private User user; // ,user field of the UserPrincipal class

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       // from dp to spring user
        List<GrantedAuthority> authorityList = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority(this.user.getRole().getDescription());
        authorityList.add(authority);
        return authorityList;
    }
    //I want to acccess user property
//pasword my entity from db ve UI dan gelip eslesecek, creating has a relasionship
    @Override
    public String getPassword() {
        return this.user.getPassWord();
    }

    @Override
    public String getUsername() {
        return this.user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }
    //conversion class, going to do mapping
}
