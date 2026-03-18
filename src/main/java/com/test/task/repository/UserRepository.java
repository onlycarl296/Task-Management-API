package com.test.task.repository;

import com.test.task.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    @Query("SELECT t FROM users t " +
            "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) " )
    Page<User> findAllByName(@Param("name") String name, Pageable pageable);
}
