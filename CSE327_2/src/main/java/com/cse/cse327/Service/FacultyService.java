package com.cse.cse327.Service;

import com.cse.cse327.Model.Faculty;
import com.cse.cse327.Reprository.FacultyRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final JavaMailSender mailSender;

    public FacultyService(FacultyRepository facultyRepository, JavaMailSender mailSender) {
        this.facultyRepository = facultyRepository;
        this.mailSender = mailSender;
    }



    public Faculty createFaculty(Faculty faculty) {
        String initial = faculty.getInitial();
        String password = faculty.getPassword();

        if (initial == null || password == null) {
            System.out.println("Empty");
        } else {
            faculty = this.facultyRepository.save(faculty);
            sendCongratulatoryEmail(faculty.getEmail()); // Send congratulatory email
        }

        return faculty;
    }

    private void sendCongratulatoryEmail(String email) {
        // Create a simple email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Congratulations on your faculty account!");
        message.setText("We would be glad to provide you service.");

        // Send the email
        mailSender.send(message);
    }
}
