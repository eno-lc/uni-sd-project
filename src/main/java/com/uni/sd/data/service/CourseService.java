package com.uni.sd.data.service;

import com.uni.sd.data.dto.CourseDto;
import com.uni.sd.data.entity.Course;
import com.uni.sd.data.repository.CourseRepository;
import com.uni.sd.mappers.CourseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {


    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public CourseDto get(Long id) {
        Optional<Course> entity = repository.findById(id);
        Course course = entity.get();
        return CourseMapper.mapToCourseDto(course);
    }

    public CourseDto update(CourseDto entity) {
        Course course = CourseMapper.mapToCourse(entity);
        Course courseSaved = repository.save(course);
        return CourseMapper.mapToCourseDto(courseSaved);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<CourseDto> list(Pageable pageable) {
        Page<Course> grades = repository.findAll(pageable);
        return grades.map(CourseMapper::mapToCourseDto);
    }

    public Page<CourseDto> list(Pageable pageable, Specification<Course> filter) {
        Page<Course> grades = repository.findAll(filter, pageable);
        return grades.map(CourseMapper::mapToCourseDto);
    }

    public List<CourseDto> findAllCourses(String filterText){

        List<Course> grades = repository.findAll();

        if(filterText == null || filterText.isEmpty()){
            return grades.stream().map(CourseMapper::mapToCourseDto).collect(Collectors.toList());
        } else {
            List<Course> gradeList = repository.search(filterText);
            return gradeList.stream()
                    .map(CourseMapper::mapToCourseDto)
                    .collect(Collectors.toList());
        }
    }
    public int count() {
        return (int) repository.count();
    }


}
