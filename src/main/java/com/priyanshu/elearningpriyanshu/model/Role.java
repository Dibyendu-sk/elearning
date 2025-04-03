package com.priyanshu.elearningpriyanshu.model;

public class Role {
    private String authority;

    // Getter and Setter methods
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Role{" +
                "authority='" + authority + '\'' +
                '}';
    }
}
