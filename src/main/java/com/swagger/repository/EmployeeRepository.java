package com.swagger.repository;

import com.swagger.entity.Employee;
import com.swagger.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,String> {
    Optional<Object> findByUserCredential(UserCredential userCredential);
}
