package com.example.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "test", schema = "planets")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "testId", nullable = false)
    private Integer id;

    @Column(name = "testName", nullable = false, length = 50)
    private String testName;

    @Column(name = "testScore")
    private Double testScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentTestId")

    private Student studentTest;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Double getTestScore() {
        return testScore;
    }

    public void setTestScore(Double testScore) {
        this.testScore = testScore;
    }

    public Student getStudentTest() {
        return studentTest;
    }

    public void setStudentTest(Student studentTest) {
        this.studentTest = studentTest;
    }

    @Override
    public String toString() {
        return testName;
    }
}