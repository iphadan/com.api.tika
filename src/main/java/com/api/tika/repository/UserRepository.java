package com.api.tika.repository;


import com.api.tika.models.USER.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

        @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
        Optional<User> findByEmail(String email);
}
