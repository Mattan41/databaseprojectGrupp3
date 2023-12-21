package com.example.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQuery(name = "student.findByName", query = "SELECT s FROM Student s WHERE s.studentName = :studentName")
@Table(name = "student", schema = "planets")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentId", nullable = false)
    private Integer id;

    @Column(name = "studentSocialSecNum", nullable = false)
    private Integer studentSocialSecNum;

    @Column(name = "studentName", length = 50)
    private String studentName;

    @Column(name = "studentAge")
    private Integer studentAge;

    @Column(name = "totResult")
    private Double totResult;

    @OneToMany(mappedBy = "studentTest")
    private Set<Test> tests = new LinkedHashSet<>();

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

    public Integer getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(Integer studentAge) {
        this.studentAge = studentAge;
    }

    public Double getTotResult() {
        return totResult;
    }

    public void setTotResult(Double totResult) {
        this.totResult = totResult;
    }

    public Set<Test> getTests() {
        return tests;
    }

    public void setTests(Set<Test> tests) {
        this.tests = tests;
    }

    @Override
    public String toString() {
        return studentName;
    }
}