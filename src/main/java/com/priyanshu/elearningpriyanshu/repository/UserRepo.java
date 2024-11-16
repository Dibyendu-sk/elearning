package com.priyanshu.elearningpriyanshu.repository;


import com.priyanshu.elearningpriyanshu.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,String> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
