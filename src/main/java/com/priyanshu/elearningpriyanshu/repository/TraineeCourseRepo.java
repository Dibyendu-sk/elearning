package com.priyanshu.elearningpriyanshu.repository;

import com.priyanshu.elearningpriyanshu.entity.TraineeCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TraineeCourseRepo extends JpaRepository<TraineeCourse,Long> {
    @Query("SELECT tc.courseId FROM TraineeCourse tc WHERE tc.traineeId=:traineeId")
    List<String> fetchEnrolledCourses(String traineeId);
    List<TraineeCourse> findByTraineeId(String traineeId);
}
