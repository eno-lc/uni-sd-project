package com.uni.sd.data.dto;

import com.uni.sd.data.entity.AbstractEntity;


public class CourseDto extends AbstractEntity {
    private String name;
    private String professor;
    private int ects;
    private int enrollmentCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEcts() {
        return ects;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public int getEnrollmentCode() {
        return enrollmentCode;
    }

    public void setEnrollmentCode(int enrollmentCode) {
        this.enrollmentCode = enrollmentCode;
    }


}
