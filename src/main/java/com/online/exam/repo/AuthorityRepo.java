package com.online.exam.repo;

import com.online.exam.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepo extends JpaRepository<Authority,Long> {
    Authority findbyAuthorityName(String name);
}
