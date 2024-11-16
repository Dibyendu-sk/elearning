package com.priyanshu.elearningpriyanshu.repository;

import com.priyanshu.elearningpriyanshu.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<CourseEntity,String> {
    @Query("SELECT c FROM CourseEntity c WHERE c.courseInstructor.id = :id")
    List<CourseEntity> fetchCources(String id);
}
