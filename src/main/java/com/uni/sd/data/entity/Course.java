package com.uni.sd.data.entity;


import javax.persistence.Entity;

@Entity
public class Course extends AbstractEntity {

    private String name;
    private String professor;
    private int ects;
    private int enrollmentCode;

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEcts() {
        return ects;
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
