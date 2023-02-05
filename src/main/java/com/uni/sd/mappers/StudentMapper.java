package com.uni.sd.mappers;

import com.uni.sd.data.dto.StudentDto;
import com.uni.sd.data.entity.Student;

public class StudentMapper {

    public static StudentDto mapToStudentDto(Student studentEntity) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(studentEntity.getId());
        studentDto.setFirstName(studentEntity.getFirstName());
        studentDto.setLastName(studentEntity.getLastName());
        studentDto.setEmail(studentEntity.getEmail());
        studentDto.setPhone(studentEntity.getPhone());
        studentDto.setDateOfBirth(studentEntity.getDateOfBirth());
        return studentDto;
    }

    public static Student mapToStudent(StudentDto studentDto) {
        Student studentEntity = new Student();
        studentEntity.setId(studentDto.getId());
        studentEntity.setFirstName(studentDto.getFirstName());
        studentEntity.setLastName(studentDto.getLastName());
        studentEntity.setEmail(studentDto.getEmail());
        studentEntity.setPhone(studentDto.getPhone());
        studentEntity.setDateOfBirth(studentDto.getDateOfBirth());
        return studentEntity;
    }

}
