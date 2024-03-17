package com.cse.cse327.Service;


import com.cse.cse327.Model.AssignedCourseDetails;
import com.cse.cse327.Model.Course;
import com.cse.cse327.Model.Faculty;
import com.cse.cse327.Model.FacultyHasCourse;
import com.cse.cse327.Reprository.AssignedCourseDetailsRepository;
import com.cse.cse327.Reprository.CourseRepository;
import com.cse.cse327.Reprository.FacultyHasCoursesRepository;
import com.cse.cse327.Reprository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyCourseService {
    @Autowired
    private FacultyHasCoursesRepository facultyHasCoursesRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AssignedCourseDetailsRepository assignedCourseDetailsRepository;

    public FacultyHasCourse setFacultyCourse(AssignedCourseDetails assignedCourseDetails, String initial, String courseCode){
        FacultyHasCourse facultyHasCourse = new FacultyHasCourse();

        Faculty faculty = this.facultyRepository.findByInitial(initial);
        Course course= this.courseRepository.findByCourseCode(courseCode);

        facultyHasCourse.setCourse(course);
        facultyHasCourse.setFaculty(faculty);
        AssignedCourseDetails details1 = this.assignedCourseDetailsRepository.save(assignedCourseDetails);
        facultyHasCourse.setAssignedCourseDetails(details1);

        this.facultyHasCoursesRepository.save(facultyHasCourse);
        return facultyHasCourse;
    }

        // ...

    public FacultyHasCourse setFacultyCourse1(AssignedCourseDetails assignedCourseDetails) {
        FacultyHasCourse facultyHasCourse = new FacultyHasCourse();

        System.out.println("I am from Course Assign");

        Faculty faculty = this.facultyRepository.findByInitial(assignedCourseDetails.getInitial());
        Course course = this.courseRepository.findByCourseCode(assignedCourseDetails.getCourseCode());

        List<FacultyHasCourse> allByFacultyInitial = this.facultyHasCoursesRepository.findAllByFaculty_Initial(assignedCourseDetails.getInitial());
        List<AssignedCourseDetails> assignedCourseDetailsList = this.assignedCourseDetailsRepository.findAll();
        int flag = 0;
        for (FacultyHasCourse fac : allByFacultyInitial) {
            if (fac.getCourse().getCourseCode().equals(assignedCourseDetails.getCourseCode()) && fac.getAssignedCourseDetails().getSection()==assignedCourseDetails.getSection()) {
                flag = 1;
                break;
            }


        }




        facultyHasCourse.setCourse(course);
        facultyHasCourse.setFaculty(faculty);
        facultyHasCourse.setAssignedCourseDetails(assignedCourseDetails);

        // Check if the total credits assigned to the faculty exceed 20
        float totalCredits = 0;
        for (FacultyHasCourse facultyHasCourse1 : allByFacultyInitial) {
            totalCredits += facultyHasCourse1.getCourse().getCredits();
        }

        totalCredits += course.getCredits();
        if (totalCredits>=11 && flag != 1) {
            flag = 2;
        }
        List<FacultyHasCourse> facultyHasCourses = facultyHasCoursesRepository.findAllByCourse_CourseCode(assignedCourseDetails.getCourseCode());



        for (FacultyHasCourse fch1 : facultyHasCourses) {
            Optional<AssignedCourseDetails> byId = this.assignedCourseDetailsRepository.findById(fch1.getAssignedCourseDetails().getId());
            if (byId.get().getSection() == assignedCourseDetails.getSection()) {
                flag = 3;
            }
        }

        if (flag == 1) {
            throw new IllegalArgumentException("Faculty is already assigned to this course");
        } else if (flag == 2) {
            throw new IllegalArgumentException("Course Limit Exceeded");
        }else if (flag == 3) {
            throw new IllegalArgumentException("Section Already Assigned");
        }
        else {
            this.assignedCourseDetailsRepository.save(assignedCourseDetails);
            this.facultyHasCoursesRepository.save(facultyHasCourse);
            return facultyHasCourse;
        }
    }


}
//, @RequestParam("initial") String initial,@RequestParam("courseCode") String courseCode