package com.cse.cse327.Model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class AssignedCourseDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int section;
    private String start;
    private String end;
    private String day;
    private String room;
    @Transient
    private String initial;
    @Transient
    private String courseCode;

    }
