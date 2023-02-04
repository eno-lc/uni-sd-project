package com.uni.sd.data.repository;

import com.uni.sd.data.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StaffRepository extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff>{

}
