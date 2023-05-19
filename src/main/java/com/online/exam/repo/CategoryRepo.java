package com.online.exam.repo;

import com.online.exam.model.Faculty;
import com.online.exam.model.Category;
import com.online.exam.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {

    List<Category> findByFaculty(Faculty faculty);
    Category findByCategoryName(String name);
    @Query(value = "select count(c) from Category c where c.users=?1",nativeQuery = true)
    Integer countCategoriesByUsers(Set<User> users);
}
