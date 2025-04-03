package com.priyanshu.elearningpriyanshu.service;

import com.priyanshu.elearningpriyanshu.entity.CourseEntity;
import com.priyanshu.elearningpriyanshu.entity.TraineeCourse;
import com.priyanshu.elearningpriyanshu.entity.UserEntity;
import com.priyanshu.elearningpriyanshu.entity.VideoEntity;
import com.priyanshu.elearningpriyanshu.exceptions.ResourceNotFoundException;
import com.priyanshu.elearningpriyanshu.model.LoggedinUserDtls;
import com.priyanshu.elearningpriyanshu.model.ViewCourseDto;
import com.priyanshu.elearningpriyanshu.repository.CourseRepo;
import com.priyanshu.elearningpriyanshu.repository.TraineeCourseRepo;
import com.priyanshu.elearningpriyanshu.repository.UserRepo;
import com.priyanshu.elearningpriyanshu.repository.VideoRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class Services {
    @Value("${video.storage.location}")
    private String storageLocation;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final TraineeCourseRepo traineeCourseRepo;
    private final VideoRepo videoRepo;
    private final Path fileStorageLocation = Paths.get("uploads/videos");
    @Value("${video.storage.location}")
    private String FILE_PATH;

    public Services(UserRepo userRepo, CourseRepo courseRepo, AuthService authService, PasswordEncoder passwordEncoder, TraineeCourseRepo traineeCourseRepo, VideoRepo videoRepo) {
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.traineeCourseRepo = traineeCourseRepo;
        this.videoRepo = videoRepo;
    }

    public Boolean signUp(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepo.save(userEntity);
        return true;
    }

    public LoggedinUserDtls login() {
        return authService.getLoggedInUserDtls();
    }

    public Boolean addCourse(MultipartFile file, String courseHeading, String courseDescription) {
        String id = authService.getLoggedInUserDtls().getId();
        UserEntity userEntity = userRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No instructor found")
        );
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setCourseDescription(courseDescription);
        courseEntity.setCourseHeading(courseHeading);
        courseEntity.setCourseInstructor(userEntity);
        courseEntity.setCreatedOn(Instant.now().getEpochSecond());
        CourseEntity save = courseRepo.save(courseEntity);

        if (save.getId()!=null) {
            VideoEntity videoEntity = storeFile(file);
            videoEntity.setCourseEntity(courseEntity);

            log.info("Saving video Entity");
            videoRepo.save(videoEntity);
        }
        return true;
    }

    public List<ViewCourseDto> viewAddedCourses() {
        LoggedinUserDtls loggedInUserDtls = authService.getLoggedInUserDtls();
        List<CourseEntity> courseEntities = courseRepo.fetchCources(loggedInUserDtls.getId());
        List<ViewCourseDto> courseDtoList = toViewCourseDtos(courseEntities);
        return courseDtoList;
    }

    public List<ViewCourseDto> viewAllCourses() {
        List<CourseEntity> courseEntities = courseRepo.findAll();
        return toViewCourseDtos(courseEntities);
    }

    public int enrollCourse(String courseId) {
        courseRepo.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course not found")
        );

        String traineeId = authService.getLoggedInUserDtls().getId();
        List<String> enrolledCourses = traineeCourseRepo.fetchEnrolledCourses(traineeId);
        if (enrolledCourses.contains(courseId)) {
            return 409;
        }
        TraineeCourse traineeCourse = new TraineeCourse();
        traineeCourse.setCourseId(courseId);
        traineeCourse.setTraineeId(traineeId);
        traineeCourse.setEnrollmentDate(Instant.now().getEpochSecond());
        traineeCourseRepo.save(traineeCourse);

        return 200;
    }

    public List<ViewCourseDto> viewEnrolledCourses() {
        String traineeId = authService.getLoggedInUserDtls().getId();
        List<String> courseIds = traineeCourseRepo.fetchEnrolledCourses(traineeId);
        List<CourseEntity> courseEntities = courseRepo.findAllById(courseIds);
        List<ViewCourseDto> viewCourseDtos = toViewCourseDtos(courseEntities);
        return viewCourseDtos;
    }

    private List<ViewCourseDto> toViewCourseDtos(List<CourseEntity> courseEntities) {
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

    public VideoEntity storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Please select a file to upload");
            }

            // Ensure the content type is video
            if (!file.getContentType().startsWith("video/")) {
                throw new RuntimeException("Only video files are allowed");
            }

            // Create storage directory if it doesn't exist
            Path uploadPath = Paths.get(storageLocation);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String videoId = UUID.randomUUID().toString();
            String uniqueFilename = videoId + fileExtension;

            // Save file
            Path filePath = uploadPath.resolve(uniqueFilename);
//            file.transferTo(filePath.toFile());
            Files.copy(file.getInputStream(),filePath);
            String absoluteFilePath = filePath.toAbsolutePath().toString();

            VideoEntity videoEntity = new VideoEntity();
            videoEntity.setId(videoId);
            videoEntity.setFilePath(absoluteFilePath);
            videoEntity.setUploadedOn(Instant.now().getEpochSecond());
            videoEntity.setContentType(file.getContentType());
            return videoEntity;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }
    public List<VideoEntity> fetchVideoByCourseId(String courseId){
        return videoRepo.fetchVideosByCourseId(courseId);
    }
    public VideoEntity fetchVideoById(String id){
        return videoRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Video not found"));
    }
}
