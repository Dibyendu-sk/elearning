package com.priyanshu.elearningpriyanshu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TAB_COURSES")
@Getter
@Setter
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String courseHeading;
    @Column(length = 500)
    private String courseDescription;
    private long createdOn;
    @ManyToOne
    private UserEntity courseInstructor;
}
