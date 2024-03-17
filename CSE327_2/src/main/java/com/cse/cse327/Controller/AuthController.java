package com.cse.cse327.Controller;

import com.cse.cse327.Jwt.JwtHelper;
import com.cse.cse327.Jwt.JwtRequest;
import com.cse.cse327.Jwt.JwtResponse;
import com.cse.cse327.Model.Course;
import com.cse.cse327.Model.Faculty;
import com.cse.cse327.Reprository.CourseRepository;
import com.cse.cse327.Reprository.FacultyHasCoursesRepository;
import com.cse.cse327.Service.CourseService;
import com.cse.cse327.Service.FacultyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FacultyHasCoursesRepository facultyHasCoursesRepository;


    @Autowired
    private JwtHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        System.out.println("I am JWT response");
        System.out.println(request.getInitial());
        System.out.println(request.getPassword());
        this.doAuthenticate(request.getInitial(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getInitial());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> addFaculty(@RequestBody Faculty faculty) {
        System.out.println("Got response");
        System.out.println(faculty.getPassword());
        if (faculty.getName()==""|| faculty.getPassword() == ""|| faculty.getInitial() == "" || faculty.getEmail() == "" ||
                faculty.getExt() ==0 || faculty.getMobile() == "" || faculty.getRoom() == "") {
            // Handle the failure case
            return new ResponseEntity<>("Required fields are missing.", HttpStatus.BAD_REQUEST);
        } else {
            // Handle the success case
            faculty.setPassword(passwordEncoder.encode(faculty.getPassword()));
            this.facultyService.createFaculty(faculty);
            return new ResponseEntity<>(faculty, HttpStatus.OK);
        }
    }

    @GetMapping("/allCourseInformation")
    public List<Object[]> getCourseInformation(){

         List<Object[]> facultyCourseDetails = this.facultyHasCoursesRepository.getFacultyCourseDetails();
         return facultyCourseDetails;
    }
}
