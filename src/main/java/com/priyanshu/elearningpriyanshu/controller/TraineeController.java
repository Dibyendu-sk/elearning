package com.priyanshu.elearningpriyanshu.controller;

import com.priyanshu.elearningpriyanshu.model.Response;
import com.priyanshu.elearningpriyanshu.model.ViewCourseDto;
import com.priyanshu.elearningpriyanshu.service.Services;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainee")
public class TraineeController {
    private final Services services;

    public TraineeController(Services services) {
        this.services = services;
    }

    @PostMapping("/enroll-course")
    @Operation(summary = "Enroll course", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<Response<String>> enrollCourse(@RequestParam String courseId){
//        boolean isEnrolled = ;
        int status = services.enrollCourse(courseId);
        if (status==200){
            return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "Course Enrolled."));
        } else if (status==409) {
            return ResponseEntity.ok(new Response<>(HttpStatus.CONFLICT.value(), "You have already enrolled this course."));
        } else {
            return ResponseEntity.ok(new Response<>(HttpStatus.EXPECTATION_FAILED.value(), "error while enrolling course."));
        }
    }
    @GetMapping("/view-enrolled-courses")
    @Operation(summary = "view enrolled courses", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<Response<List<ViewCourseDto>>> viewEnrolledCourses(){
        List<ViewCourseDto> courseDtoList = services.viewEnrolledCourses();
        return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), courseDtoList));
    }
    @GetMapping("/view-all-courses")
    @Operation(summary = "view all courses", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<Response<List<ViewCourseDto>>> viewAllCourses(){
        List<ViewCourseDto> courseDtoList = services.viewAllCourses();
        return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), courseDtoList));
    }
}
