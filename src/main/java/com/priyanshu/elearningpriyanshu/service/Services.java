package com.priyanshu.elearningpriyanshu.service;

import com.priyanshu.elearningpriyanshu.entity.CourseEntity;
import com.priyanshu.elearningpriyanshu.entity.UserEntity;
import com.priyanshu.elearningpriyanshu.exceptions.ResourceNotFoundException;
import com.priyanshu.elearningpriyanshu.model.AddCourseDto;
import com.priyanshu.elearningpriyanshu.model.LoggedinUserDtls;
import com.priyanshu.elearningpriyanshu.model.ViewCourseDto;
import com.priyanshu.elearningpriyanshu.repository.CourseRepo;
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

    public Services(UserRepo userRepo, CourseRepo courseRepo, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
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
    public List<ViewCourseDto> viewCourses(){
        LoggedinUserDtls loggedInUserDtls = authService.getLoggedInUserDtls();
        List<CourseEntity> courseEntities = courseRepo.fetchCources(loggedInUserDtls.getId());
//        List<ViewCourseDto> courseDtoList = new ArrayList<>();
        List<ViewCourseDto> courseDtoList = courseEntities.stream().map(courseEntity -> {
            ViewCourseDto viewCourseDto = new ViewCourseDto();
            viewCourseDto.setCourseDescription(courseEntity.getCourseDescription());
            viewCourseDto.setCourseHeading(courseEntity.getCourseHeading());
            viewCourseDto.setCourseInstructor(loggedInUserDtls.getFirstName() + " " + loggedInUserDtls.getLastName());
            viewCourseDto.setCreatedOn(courseEntity.getCreatedOn());
            return viewCourseDto;
        }).toList();
        return courseDtoList;
    }
}
