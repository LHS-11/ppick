package com.warrenverr.ppick.repository;

import com.warrenverr.ppick.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
