package com.warrenverr.ppick.repository;

import com.warrenverr.ppick.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findBySnsid(String snsid);

}

