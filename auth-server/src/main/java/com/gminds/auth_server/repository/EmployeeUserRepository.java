package com.gminds.auth_server.repository;

import com.gminds.auth_server.model.EmployeeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeUserRepository extends JpaRepository<EmployeeUser, Long> {
    EmployeeUser findByUsername(String username);

}
