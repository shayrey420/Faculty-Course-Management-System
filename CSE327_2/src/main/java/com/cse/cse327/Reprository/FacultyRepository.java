package com.cse.cse327.Reprository;
import com.cse.cse327.Model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, String> {
    Faculty findByInitial(String initial);


}
