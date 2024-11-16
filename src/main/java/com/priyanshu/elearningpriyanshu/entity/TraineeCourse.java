package com.priyanshu.elearningpriyanshu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TRAINEE_COURSE")
@Getter
@Setter
@NoArgsConstructor
public class TraineeCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trainee_id", nullable = false)
    private String traineeId;

    @Column(name = "course_id", nullable = false)
    private String courseId;

    @Column(name = "enrollment_date")
    private long enrollmentDate;
}
