package com.cse.cse327.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class FacultyHasCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JsonIgnore
    private Faculty faculty;

    @ManyToOne
    @JsonIgnore
    private Course course;

    @OneToOne(cascade = CascadeType.ALL)
    private AssignedCourseDetails assignedCourseDetails;

}
