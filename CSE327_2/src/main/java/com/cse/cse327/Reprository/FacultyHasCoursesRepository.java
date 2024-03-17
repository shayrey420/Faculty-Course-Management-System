package com.cse.cse327.Reprository;

import com.cse.cse327.Model.FacultyHasCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FacultyHasCoursesRepository extends JpaRepository<FacultyHasCourse,Integer> {
    List<FacultyHasCourse> findAllByFaculty_Initial(String initial);
    List<FacultyHasCourse> findAllByCourse_CourseCode(String initial);

    @Query("SELECT f.initial, c.courseCode, acd.section, acd.start, acd.end, acd.day, acd.room " +
            "FROM FacultyHasCourse fhc " +
            "JOIN fhc.faculty f " +
            "JOIN fhc.course c " +
            "JOIN fhc.assignedCourseDetails acd")
    List<Object[]> getFacultyCourseDetails();

    @Query("SELECT f.initial, c.courseCode,c.credits,c.title, acd.section, acd.start, acd.end, acd.day, acd.room " +
            "FROM FacultyHasCourse fhc " +
            "JOIN fhc.faculty f " +
            "JOIN fhc.course c " +
            "JOIN fhc.assignedCourseDetails acd " +
            "WHERE f.initial = :initial") // Add the WHERE clause to filter by faculty initial
    List<Object[]> getFacultyCourse(@Param("initial") String initial);


    FacultyHasCourse findByCourse_CourseCodeAndAssignedCourseDetails_Section(String courseCode, int section);
}
