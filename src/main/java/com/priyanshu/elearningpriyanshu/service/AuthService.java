package com.priyanshu.elearningpriyanshu.service;

import com.priyanshu.elearningpriyanshu.model.LoggedinUserDtls;
import com.priyanshu.elearningpriyanshu.model.UserDetailsModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthService {
    public LoggedinUserDtls getLoggedInUserDtls() {
        // Fetch the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsModel) {
                LoggedinUserDtls loggedinUserDtls = new LoggedinUserDtls();
                // Get email (username) from the UserDetails object
                loggedinUserDtls.setUserName(((UserDetailsModel) principal).getUsername());
                loggedinUserDtls.setEmail(((UserDetailsModel) principal).getEmail());
                loggedinUserDtls.setId(((UserDetailsModel) principal).getId());
                loggedinUserDtls.setFirstName(((UserDetailsModel) principal).getFirstName());
                loggedinUserDtls.setLastName(((UserDetailsModel) principal).getLastName());
                Map<String,Object> map = new HashMap<>();
                map.put("role",((UserDetailsModel) principal).getAuthorities());
                loggedinUserDtls.setRole(map);
                return loggedinUserDtls;
            } else {
                // In case the principal is just a string (e.g., anonymous or simple login)
                return null;
            }
        }
        return null;
    }
}
