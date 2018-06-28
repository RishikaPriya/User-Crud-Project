package com.example.demo.repository;

import com.example.demo.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM User u WHERE u.name = ?1 ORDER BY u.id LIMIT ?2" , nativeQuery = true)
    List<User> findUser(String name, int limit);
}
