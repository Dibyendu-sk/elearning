package com.priyanshu.elearningpriyanshu.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AddCourseDto {
    private String courseHeading;
    private String courseDescription;
    private MultipartFile file;
}
