package com.cse.cse327.Service;

import com.cse.cse327.Model.Course;
import com.cse.cse327.Reprository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    public Course createCourse(Course Course) {
        Course course1 = null;
        List<Course> allCourse = this.courseRepository.findAll();
        System.out.println("HERE");

        for (Course course : allCourse) {
            if (course.getCourseCode().equals(Course.getCourseCode())) {
                System.out.println("Course Already there");
            } else {

                course1 = this.courseRepository.save(Course);
                System.out.println("Course Saved");


            }
        }
        if(allCourse.isEmpty()){
            course1 = this.courseRepository.save(Course);
            System.out.println("Course Saved");
        }
        return course1;

    }
}
