package com.priyanshu.elearningpriyanshu.service;

import com.priyanshu.elearningpriyanshu.entity.CourseEntity;
import com.priyanshu.elearningpriyanshu.entity.TraineeCourse;
import com.priyanshu.elearningpriyanshu.entity.UserEntity;
import com.priyanshu.elearningpriyanshu.exceptions.ResourceNotFoundException;
import com.priyanshu.elearningpriyanshu.model.AddCourseDto;
import com.priyanshu.elearningpriyanshu.model.LoggedinUserDtls;
import com.priyanshu.elearningpriyanshu.model.ViewCourseDto;
import com.priyanshu.elearningpriyanshu.repository.CourseRepo;
import com.priyanshu.elearningpriyanshu.repository.TraineeCourseRepo;
import com.priyanshu.elearningpriyanshu.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class Services {
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final TraineeCourseRepo traineeCourseRepo;

    public Services(UserRepo userRepo, CourseRepo courseRepo, AuthService authService, PasswordEncoder passwordEncoder, TraineeCourseRepo traineeCourseRepo) {
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.traineeCourseRepo = traineeCourseRepo;
    }

    public Boolean signUp(UserEntity userEntity){
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepo.save(userEntity);
        return true;
    }
    public LoggedinUserDtls login(){
        return authService.getLoggedInUserDtls();
    }
    public Boolean addCourse(AddCourseDto addCourseDto){
        String id = authService.getLoggedInUserDtls().getId();
        UserEntity userEntity = userRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No instructor found")
        );
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setCourseDescription(addCourseDto.getCourseDescription());
        courseEntity.setCourseHeading(addCourseDto.getCourseHeading());
        courseEntity.setCourseInstructor(userEntity);
        courseEntity.setCreatedOn(Instant.now().getEpochSecond());
        courseRepo.save(courseEntity);

        return true;
    }
    public List<ViewCourseDto> viewAddedCourses(){
        LoggedinUserDtls loggedInUserDtls = authService.getLoggedInUserDtls();
        List<CourseEntity> courseEntities = courseRepo.fetchCources(loggedInUserDtls.getId());
        List<ViewCourseDto> courseDtoList = toViewCourseDtos(courseEntities);
        return courseDtoList;
    }
    public List<ViewCourseDto> viewAllCourses(){
        List<CourseEntity> courseEntities = courseRepo.findAll();
        return toViewCourseDtos(courseEntities);
    }
    public boolean enrollCourse(String courseId){
        courseRepo.findById(courseId).orElseThrow(
                ()->new ResourceNotFoundException("Course not found")
        );
        String traineeId = authService.getLoggedInUserDtls().getId();
        TraineeCourse traineeCourse = new TraineeCourse();
        traineeCourse.setCourseId(courseId);
        traineeCourse.setTraineeId(traineeId);
        traineeCourse.setEnrollmentDate(Instant.now().getEpochSecond());
        traineeCourseRepo.save(traineeCourse);

        return true;
    }
    public List<ViewCourseDto> viewEnrolledCourses(){
        String traineeId = authService.getLoggedInUserDtls().getId();
        List<String> courseIds = traineeCourseRepo.fetchEnrolledCourses(traineeId);
        List<CourseEntity> courseEntities = courseRepo.findAllById(courseIds);
        List<ViewCourseDto> viewCourseDtos = toViewCourseDtos(courseEntities);
        return viewCourseDtos;
    }

    private List<ViewCourseDto> toViewCourseDtos(List<CourseEntity> courseEntities){
        return courseEntities.stream().map(courseEntity -> {
            ViewCourseDto viewCourseDto = new ViewCourseDto();
            viewCourseDto.setCourseId(courseEntity.getId());
            viewCourseDto.setCourseDescription(courseEntity.getCourseDescription());
            viewCourseDto.setCourseHeading(courseEntity.getCourseHeading());
            viewCourseDto.setCourseInstructor(courseEntity.getCourseInstructor().getFirstName() + " " + courseEntity.getCourseInstructor().getLastName());
            viewCourseDto.setCreatedOn(courseEntity.getCreatedOn());
            return viewCourseDto;
        }).toList();
    }
}
