package com.example.yurnero.demo.repository;

import com.example.yurnero.demo.enums.UserRole;
import com.example.yurnero.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String username);
    Optional<User> findByUserRole(UserRole userRole);
}
