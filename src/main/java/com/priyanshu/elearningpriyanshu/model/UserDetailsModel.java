package com.priyanshu.elearningpriyanshu.model;


import com.priyanshu.elearningpriyanshu.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class UserDetailsModel implements UserDetails {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<GrantedAuthority> authorities;
    private String id;

    public UserDetailsModel(UserEntity user){
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.authorities = Stream.of(user.getRole().split(",")).map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim())).collect(Collectors.toList());
        this.email = user.getEmail();
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
