package com.uni.sd.data.entity;


import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import java.time.LocalDate;

@Entity
public class Professor extends AbstractEntity{

    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String subject;
    @Nullable
    private String post;
    @Nullable
    private String image;

    @Nullable
    private String lectureContent;

    @Nullable
    public String getLectureContent() {
        return lectureContent;
    }

    public void setLectureContent(@Nullable String lectureContent) {
        this.lectureContent = lectureContent;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
