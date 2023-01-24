package com.online.exam.repo;

import com.online.exam.model.Faculty;
import com.online.exam.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    List<Category> findByFaculty(Faculty faculty);
    Category findByCategoryName(String name);
}
