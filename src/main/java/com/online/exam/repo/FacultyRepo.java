package com.online.exam.repo;

import com.online.exam.model.Faculty;
import com.online.exam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyRepo extends JpaRepository<Faculty,Long> {
    Faculty findByFacultyName(String facName);



}
