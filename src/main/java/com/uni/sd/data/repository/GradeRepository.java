package com.uni.sd.data.repository;

import com.uni.sd.data.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long>, JpaSpecificationExecutor<Grade> {

    @Query("select c from Grade c " +
            "where lower(c.student) like lower(concat('%', :searchTerm, '%')) ")
    List<Grade> search(@Param("searchTerm") String searchTerm);

}
