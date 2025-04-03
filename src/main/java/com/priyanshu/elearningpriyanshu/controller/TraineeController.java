package com.priyanshu.elearningpriyanshu.controller;

import com.priyanshu.elearningpriyanshu.entity.VideoEntity;
import com.priyanshu.elearningpriyanshu.model.Response;
import com.priyanshu.elearningpriyanshu.model.ViewCourseDto;
import com.priyanshu.elearningpriyanshu.service.Services;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
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
    @GetMapping("/view-resources/{courseId}")
    public ResponseEntity<Response<List<VideoEntity>>> viewResources(@PathVariable String courseId){
        List<VideoEntity> videoEntities = services.fetchVideoByCourseId(courseId);
        return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), videoEntities));
    }
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> streamVideo(
            @PathVariable String videoId,
            @RequestHeader(value = "Range",required = false) String range) {

        log.info("Range - "+range);
            VideoEntity videoEntity = services.fetchVideoById(videoId);
            Path videoPath = Paths.get(videoEntity.getFilePath());
//            Resource videoResource = new UrlResource(videoPath.toUri());
            Resource videoResource = new FileSystemResource(videoPath);
//            long fileLength = videoPath.toFile().length();
            String contentType = videoEntity.getContentType();
//
//
//            if (range == null) {
//                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(videoResource);
//            }
//            long rangeStart;
//            long rangeEnd;
//            String[] ranges = range.replace("bytes ", "").split("-");
//            rangeStart=Long.parseLong(ranges[0]);
//            if (ranges.length > 1){
//                rangeEnd = Long.parseLong(ranges[1]);
//            }else {
//                rangeEnd = fileLength - 1;
//            }
//            if (rangeEnd > fileLength - 1){
//                rangeEnd = fileLength - 1;
//            }
//            InputStream inputStream;
//            try{
//                inputStream = Files.newInputStream(videoPath);
//                inputStream.skip(rangeStart);
//            }catch (IOException ex){
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//            long contentLength = rangeEnd-rangeStart+1;
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentLength(contentLength);
//        headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//        headers.add("X-Content-Type-Options", "nosniff");
//            return ResponseEntity
//                    .status(HttpStatus.PARTIAL_CONTENT)
//                    .headers(headers)
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .body(new InputStreamResource(inputStream));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(videoResource);
    }
}
