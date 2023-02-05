package com.uni.sd.data.service;

import com.uni.sd.data.dto.StudentDto;
import com.uni.sd.data.entity.Professor;
import com.uni.sd.data.entity.Student;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.uni.sd.data.repository.StudentRepository;
import com.uni.sd.mappers.StudentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public StudentDto get(Long id) {
        Optional<Student> entity = repository.findById(id);
        Student student = entity.get();
        return StudentMapper.mapToStudentDto(student);
    }

    public StudentDto update(StudentDto entity) {
        Student student = StudentMapper.mapToStudent(entity);
        Student studentSaved = repository.save(student);
        return StudentMapper.mapToStudentDto(studentSaved);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<StudentDto> list(Pageable pageable) {
        Page<Student> students = repository.findAll(pageable);
        return students.map(StudentMapper::mapToStudentDto);
    }

    public Page<StudentDto> list(Pageable pageable, Specification<Student> filter) {
        Page<Student> students = repository.findAll(filter, pageable);
        return students.map(StudentMapper::mapToStudentDto);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<StudentDto> findALlStudents(String filterText){

        List<Student> students = repository.findAll();

        if(filterText == null || filterText.isEmpty()){
            return students.stream().map(StudentMapper::mapToStudentDto).collect(Collectors.toList());
        } else {
            List<Student> studentList = repository.search(filterText);
            return studentList.stream()
                    .map(StudentMapper::mapToStudentDto)
                    .collect(Collectors.toList());
        }
    }
}
