package com.uni.sd.data.repository;

import com.uni.sd.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends
            JpaRepository<Student, Long>,
            JpaSpecificationExecutor<Student> {

}
