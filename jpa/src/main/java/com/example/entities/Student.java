package com.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "student", schema = "planeter")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentId", nullable = false)
    private Integer id;

    @Column(name = "studentSocialSecNum")
    private Integer studentSocialSecNum;

    @Column(name = "studentName", length = 50)
    private String studentName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentSocialSecNum() {
        return studentSocialSecNum;
    }

    public void setStudentSocialSecNum(Integer studentSocialSecNum) {
        this.studentSocialSecNum = studentSocialSecNum;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return "Student{" +
               "id=" + id +
               ", studentSocialSecNum=" + studentSocialSecNum +
               ", studentName='" + studentName + '\'' +
               '}';
    }
}