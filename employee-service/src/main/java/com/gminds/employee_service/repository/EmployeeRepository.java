package com.gminds.employee_service.repository;


import com.gminds.employee_service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e JOIN FETCH e.job j JOIN FETCH j.department d  WHERE e.id = :id")
    Optional<Employee> findByIdWithJobAndDepartment(@Param("id") Long id);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.agreements WHERE e.name = :name AND e.surname = :surname")
    Optional<Employee> findByNameAndSurnameWithAgreements(String name, String surname);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.certificates WHERE e.name = :name AND e.surname = :surname")
    Optional<Employee> findByNameAndSurnameWithCertificates(String name, String surname);

}
