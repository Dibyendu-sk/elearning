package com.priyanshu.elearningpriyanshu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TAB_VIDEOS")
@Getter
@Setter
public class VideoEntity {
    @Id
    private String id;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "file_path")
    private String filePath;
    private String sequence;
    @ManyToOne
    private CourseEntity courseEntity;
    private long uploadedOn;
    private long updatedOn;
}
