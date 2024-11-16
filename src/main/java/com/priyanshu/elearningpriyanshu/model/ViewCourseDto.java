package com.priyanshu.elearningpriyanshu.model;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ViewCourseDto {
    private String courseHeading;
    private String courseDescription;
    private String courseInstructor;
    private long createdOn;
}
