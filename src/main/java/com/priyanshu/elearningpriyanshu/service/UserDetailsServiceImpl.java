package com.priyanshu.elearningpriyanshu.service;

import com.priyanshu.elearningpriyanshu.entity.UserEntity;
import com.priyanshu.elearningpriyanshu.model.UserDetailsModel;
import com.priyanshu.elearningpriyanshu.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepo.findByEmail(username);
        return user.map(UserDetailsModel::new).orElseThrow(()->new UsernameNotFoundException("Invalid Username"));
    }
}
