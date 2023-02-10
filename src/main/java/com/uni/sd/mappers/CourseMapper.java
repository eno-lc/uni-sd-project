package com.uni.sd.mappers;

import com.uni.sd.data.dto.CourseDto;
import com.uni.sd.data.entity.Course;

public class CourseMapper {

    public static CourseDto mapToCourseDto(Course courseEntity) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(courseEntity.getId());
        courseDto.setName(courseEntity.getName());
        courseDto.setProfessor(courseEntity.getProfessor());
        courseDto.setEcts(courseEntity.getEcts());
        courseDto.setEnrollmentCode(courseEntity.getEnrollmentCode());

        return courseDto;
    }

    public static Course mapToCourse(CourseDto courseDto) {
        Course courseEntity = new Course();
        courseEntity.setId(courseDto.getId());
        courseEntity.setName(courseDto.getName());
        courseEntity.setEcts(courseDto.getEcts());
        courseEntity.setProfessor(courseDto.getProfessor());
        courseEntity.setEnrollmentCode(courseDto.getEnrollmentCode());

        return courseEntity;
    }

}
