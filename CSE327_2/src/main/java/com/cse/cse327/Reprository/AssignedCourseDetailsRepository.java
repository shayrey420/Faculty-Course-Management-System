package com.cse.cse327.Reprository;


import com.cse.cse327.Model.AssignedCourseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AssignedCourseDetailsRepository extends JpaRepository<AssignedCourseDetails, Integer> {

}
