package com.cse.cse327.Controller;

import com.cse.cse327.Model.AssignedCourseDetails;
import com.cse.cse327.Model.Course;
import com.cse.cse327.Model.Faculty;
import com.cse.cse327.Model.FacultyHasCourse;
import com.cse.cse327.Reprository.AssignedCourseDetailsRepository;
import com.cse.cse327.Reprository.CourseRepository;
import com.cse.cse327.Reprository.FacultyHasCoursesRepository;
import com.cse.cse327.Reprository.FacultyRepository;
import com.cse.cse327.Service.CourseService;
import com.cse.cse327.Service.FacultyCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class HomeController {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyCourseService facultyCourseService;
    @Autowired
    private AssignedCourseDetailsRepository assignedCourseDetailsRepository;
    @Autowired
    private FacultyHasCoursesRepository facultyHasCoursesRepository;

    @GetMapping("/Allcourses")
    public List<Course> getAllCourse(){
        System.out.println("HELLO");
        return courseRepository.findAll();
    }
    @PostMapping("/addCourse")
    public ResponseEntity<Object> addCourse(@RequestBody Course course){

        if (course.getCourseCode()==""|| course.getParallelCourseType() == ""|| course.getType() == "" || course.getTitle() == "" ||
                course.getCredits() ==0 || course.getTitle() == "") {
            // Handle the failure case
            return new ResponseEntity<>("Missing.", HttpStatus.BAD_REQUEST);
        } else {

            this.courseService.createCourse(course);
            return new ResponseEntity<>(course, HttpStatus.OK);
        }
    }
    @GetMapping("/initial")
    public Faculty getFaculty(Principal principal){
        System.out.println("get call");
        String name = principal.getName();
        System.out.println("Principal:"+name);
        return this.facultyRepository.findByInitial(name);
    }
    @PostMapping("/assignCourse")
    public ResponseEntity<String> facultyHasCourse(@RequestBody AssignedCourseDetails assignedCourseDetails) {
        try {
            FacultyHasCourse facultyHasCourse = this.facultyCourseService.setFacultyCourse1(assignedCourseDetails);
            return ResponseEntity.ok("Course assigned successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/allFaculties")
    public List<Faculty> getAllFaculty(){
        System.out.println("Hello faculty");
        return this.facultyRepository.findAll();
    }

    @GetMapping("/facultyCourses")
    public List<Object[]> getCoursesOfFaculty(Principal principal){
        return this.facultyHasCoursesRepository.getFacultyCourse(principal.getName());
    }

    @DeleteMapping("/dropCourse/{courseCode}/{section}")
    public ResponseEntity<String> dropCourse(@PathVariable String courseCode, @PathVariable int section) {
        // Find the FacultyHasCourse entity with the given course code and section
        FacultyHasCourse facultyHasCourse = this.facultyHasCoursesRepository.findByCourse_CourseCodeAndAssignedCourseDetails_Section(
                courseCode, section);
        if (facultyHasCourse != null) {
//            // Delete the assigned course with the specified section and course code
//            facultyHasCourse.setAssignedCourseDetails(null);
            this.facultyHasCoursesRepository.deleteById(facultyHasCourse.getId());
            return ResponseEntity.ok("Assigned course dropped successfully");
        }
        return ResponseEntity.badRequest().body("Failed to drop assigned course");
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(Principal principal) {
        try {
            // Get the faculty's initial from the authentication context
            String facultyInitial = principal.getName();

            // Get all courses assigned to the faculty
            List<FacultyHasCourse> facultyCourses = facultyHasCoursesRepository.findAllByFaculty_Initial(facultyInitial);

            int totalCredits = 0;
            for (FacultyHasCourse facultyCourse : facultyCourses) {
                totalCredits += facultyCourse.getCourse().getCredits();
            }

            int totalCourses = facultyCourses.size();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("totalCredits", totalCredits);
            responseData.put("totalCourses", totalCourses);
            responseData.put("initial",principal.getName());

            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching dashboard data");
        }
    }

}
