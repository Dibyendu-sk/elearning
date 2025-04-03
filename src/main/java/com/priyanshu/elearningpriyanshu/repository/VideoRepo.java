package com.priyanshu.elearningpriyanshu.repository;

import com.priyanshu.elearningpriyanshu.entity.CourseEntity;
import com.priyanshu.elearningpriyanshu.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepo extends JpaRepository<VideoEntity,String> {
    @Query("SELECT v FROM VideoEntity v WHERE v.courseEntity.id = :courseId")
    List<VideoEntity> fetchVideosByCourseId(String courseId);
}
