package com.gminds.employee_service.repository;


import com.gminds.employee_service.model.EmployeeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeUserRepository extends JpaRepository<EmployeeUser,Long> {
    EmployeeUser findByUsername(String username);

}
