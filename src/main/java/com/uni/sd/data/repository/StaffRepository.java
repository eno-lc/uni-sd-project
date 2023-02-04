package com.uni.sd.data.repository;

import com.uni.sd.data.entity.Professor;
import com.uni.sd.data.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff>{

    @Query("select c from Staff c " +
            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Staff> search(@Param("searchTerm") String searchTerm);

}
