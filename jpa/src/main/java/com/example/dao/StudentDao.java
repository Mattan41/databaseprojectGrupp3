package com.example.dao;

import com.example.JPAUtil;
import com.example.entities.Planet;
import com.example.entities.Student;
import com.example.util.InputReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class StudentDao {
    // Visa alla studenter
    public void showAllStudents() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
            List<Student> students = query.getResultList();
            students.forEach(System.out::println);
        } catch (Exception e) {
            handleException(e);
        } finally {
            em.close();
        }
    }

    public static void insertStudent() {
        EntityManager em = JPAUtil.getEntityManager();

        var studentName = InputReader.inputString("Enter the students name: ");
        var studentSocSecNr = InputReader.inputInt("Enter the students Social Security Number: ");
        var studentAge = InputReader.inputInt("Enter the students Age: ");
        var totResult = InputReader.inputDouble("Enter the students total Score: ");

        // Validate user input
        if (studentName == null || studentSocSecNr <= 0 || studentAge <= 0 || totResult < 0) {
            System.out.println("Invalid input.");
            return;
        }

        try {
            em.getTransaction().begin();

            Student student = new Student();
            student.setStudentName(studentName);
            student.setStudentAge(studentAge);
            student.setStudentSocialSecNum(studentSocSecNr);
            student.setTotResult(totResult);

            em.persist(student);
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
