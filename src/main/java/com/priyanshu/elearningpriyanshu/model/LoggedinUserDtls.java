package com.priyanshu.elearningpriyanshu.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoggedinUserDtls {
    private String firstName;
    private String lastName;
    private String id;
    private String email;
    private String userName;
    private Map<String,Object> role;
}
