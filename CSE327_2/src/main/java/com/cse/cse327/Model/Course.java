package com.cse.cse327.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Course {

    @Id
    private String courseCode;
    private String title;
    private float credits;
    private String type;
    private String parallelCourseType;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<FacultyHasCourse> facultyHasCourses=new LinkedList<>();

}
