package com.priyanshu.elearningpriyanshu.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoggedInUserDetails {
    private String email;
    private String phoneNumber;
    private String role;
}
