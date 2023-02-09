package com.uni.sd.data.repository;

import com.uni.sd.data.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long>, JpaSpecificationExecutor<Course> {

    @Query("select c from Course c " +
            "where lower(c.name) like lower(concat('%', :searchTerm, '%')) ")
    List<Course> search(@Param("searchTerm") String searchTerm);


}
