package com.cse.cse327.Reprository;

import com.cse.cse327.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseRepository extends JpaRepository<Course, String> {
    Course findByCourseCode(String initial);
}
