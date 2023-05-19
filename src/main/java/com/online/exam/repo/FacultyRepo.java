package com.online.exam.repo;

import com.online.exam.model.Faculty;
import com.online.exam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface FacultyRepo extends JpaRepository<Faculty,Long> {
    Faculty findByFacultyName(String facName);
    @Query(value = "select count(f) from Faculty f where f.users=?1",nativeQuery = true)
    Integer countFacultiesByUsers(Set<User> users);



}
