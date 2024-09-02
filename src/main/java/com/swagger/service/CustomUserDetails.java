package com.swagger.service;

import com.swagger.entity.UserCredential;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
//    private final Long role;
    private UserCredential userCredential;


    public CustomUserDetails(UserCredential userCredential) {
        this.username = userCredential.getEmail();
        this.password = userCredential.getPassword();
//        this.role = role;
        this.userCredential = userCredential;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.emptyList();
//    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> userCredential.getAuthority().getRole());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
}
