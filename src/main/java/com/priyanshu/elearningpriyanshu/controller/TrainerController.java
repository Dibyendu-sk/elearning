package com.priyanshu.elearningpriyanshu.controller;

import com.priyanshu.elearningpriyanshu.entity.CourseEntity;
import com.priyanshu.elearningpriyanshu.model.AddCourseDto;
import com.priyanshu.elearningpriyanshu.model.Response;
import com.priyanshu.elearningpriyanshu.model.ViewCourseDto;
import com.priyanshu.elearningpriyanshu.service.Services;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/trainer")
@RestController
public class TrainerController {
    private final Services services;

    public TrainerController(Services services) {
        this.services = services;
    }

    @PostMapping(value = "/add-course",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Add course", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<Response<String>> addCourse(@RequestParam("file") MultipartFile file,@RequestParam String courseHeading,@RequestParam String courseDescription){
        Boolean isAdded = services.addCourse(file, courseHeading, courseDescription);
        if (isAdded){
            return ResponseEntity.ok(new Response<>(HttpStatus.CREATED.value(), "Course added"));
        }
        else {
            return ResponseEntity.ok(new Response<>(HttpStatus.EXPECTATION_FAILED.value(), "error while adding course"));
        }
    }
    @GetMapping ("/view-individual-courses")
    @Operation(summary = "View courses", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<Response<List<ViewCourseDto>>> viewCourses(){
        List<ViewCourseDto> courseDtoList = services.viewAddedCourses();
        return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), courseDtoList));
    }
}
