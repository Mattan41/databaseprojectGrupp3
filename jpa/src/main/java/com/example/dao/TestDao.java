package com.example.dao;

import com.example.Main;
import com.example.entities.*;
import com.example.JPAUtil;
import com.example.entities.Test;
import com.example.util.InputReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;


import java.util.List;

public class TestDao {


    public void insertTestInput() {

        String testName = InputReader.inputString("Enter test name: ");
        Double testScore = InputReader.inputDouble("Enter test score: ");
        Integer studentTestId = InputReader.inputInt("Enter student test ID: ");

        insertTest(testName, testScore, studentTestId);
        System.out.println(testName + " added for student");
    }

    private static void insertTest(String testName, Double testScore, Integer studentTestId) {
        Main.inTransaction(EntityManager -> {
                Test test = new Test();
                test.setTestName(testName);
                test.setTestScore(testScore);
                EntityManager.persist(test);
                Student student = EntityManager.find(Student.class, studentTestId);
                if (student == null) {
                System.out.println("Student not found.");
                return;
            }
                test.setStudentTest(student);
            });


    }

    // Visa alla tester
    public void showAllTests() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Test> query = em.createQuery("SELECT t FROM Test t", Test.class);
            List<Test> tests = query.getResultList();
            tests.forEach(System.out::println);
        } catch (Exception e) {
            handleException(e);
        } finally {
            em.close();
        }
    }

    private static void handleException(Exception e) {
        e.printStackTrace();
    }
}
