package com.priyanshu.elearningpriyanshu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollment {
    private String courseId;
    private String enrollmentDate;
}
