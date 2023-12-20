package com.example.dao;

import com.example.entities.*;
import com.example.JPAUtil;
import com.example.entities.Test;
import com.example.util.InputReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;

public class TestDao {
     static EntityManager em = JPAUtil.getEntityManager();

    public static void createTest() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter test name to add: ");
        String testName = scanner.nextLine();
        System.out.print("Enter test score: ");
        Double testScore = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter student test ID: ");
        Integer studentTestId = Integer.parseInt(scanner.nextLine());

        if (testName == null || testName.isEmpty() || testScore <= 0) {
            System.out.println("Invalid input.");
            return;
        }

        Test test = new Test();
        test.setTestName(testName);
        test.setTestScore(testScore);

        Student student = em.find(Student.class, studentTestId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        test.setStudentTest(student);

        var transaction = em.getTransaction();
        transaction.begin();
        em.persist(test);
        transaction.commit();
        em.close();
    }

    // Visa alla tester
    public void showAllTests() {
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

    public void insertTest() {
        EntityManager em = JPAUtil.getEntityManager();

        var testName = InputReader.inputString("Enter the test: ");
        var testScore = InputReader.inputDouble("Enter the score: ");
        var student = InputReader.inputString("Enter the student: ");
        // TODO: MATS - Här tänker jag att man får hämta studentTestId från student -> metod i inputReader?



        try {
            em.getTransaction().begin();

            Test test = new Test();
            test.setTestName(testName);
            test.setTestScore(testScore);
            //test.setStudentTest(studentTestId);


            em.persist(test);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }


    private static void handleException(Exception e) {
        e.printStackTrace();
    }
}
