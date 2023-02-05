package com.uni.sd.mappers;

import com.uni.sd.data.dto.GradeDto;
import com.uni.sd.data.entity.Grade;

public class GradeMapper {


    public static GradeDto mapToGradeDto(Grade gradeEntity) {
        GradeDto gradeDto = new GradeDto();
        gradeDto.setId(gradeEntity.getId());
        gradeDto.setCourse(gradeEntity.getCourse());
        gradeDto.setStudent(gradeEntity.getStudent());
        gradeDto.setGrade(gradeEntity.getGrade());
        gradeDto.setProfessor(gradeEntity.getProfessor());
        gradeDto.setDateAssigned(gradeEntity.getDateAssigned());
        return gradeDto;
    }

    public static Grade mapToGrade(GradeDto gradeDto) {
        Grade gradeEntity = new Grade();
        gradeEntity.setId(gradeDto.getId());
        gradeEntity.setCourse(gradeDto.getCourse());
        gradeEntity.setStudent(gradeDto.getStudent());
        gradeEntity.setGrade(gradeDto.getGrade());
        gradeEntity.setProfessor(gradeDto.getProfessor());
        gradeEntity.setDateAssigned(gradeDto.getDateAssigned());
        return gradeEntity;
    }

}
