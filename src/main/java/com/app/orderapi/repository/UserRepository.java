package com.app.orderapi.repository;

import com.app.orderapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u from User u WHERE " +
            "u.firstName LIKE CONCAT('%',:query,'%') " +
            "or u.lastName LIKE CONCAT('%',:query,'%')")
    List<User> searchUsers(String query);

    User findByEmailIdIgnoreCase(String email);
    Boolean existsByEmailId(String email);

}
