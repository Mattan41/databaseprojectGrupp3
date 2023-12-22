package com.example.dao;

import com.example.JPAUtil;
import com.example.Main;
import com.example.entities.Student;
import com.example.dtos.StudentDto;
import com.example.entities.Test;
import com.example.util.InputReader;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class StudentDao {

    //Create - add student
    public void insertStudentInput() {

        var studentSocSecNr = InputReader.inputInt("Enter the students Social Security Number: ");
        if (checkIfStudentExist(studentSocSecNr)) return;
        var studentName = InputReader.inputString("Enter the students name: ");
        var studentAge = InputReader.inputInt("Enter the students Age: ");
        var totResult = InputReader.inputDouble("Enter the students total Score: ");

        insertStudent(studentName, studentAge, studentSocSecNr, totResult);
    }
    private static boolean checkIfStudentExist(int studentSocSecNr) {
        if(studentIdExist(studentSocSecNr)){
            System.out.println("Student already in database");
            return true;
        }
        return false;
    }
    private static void insertStudent(String studentName, int studentAge, int studentSocSecNr, double totResult) {
        Main.inTransaction(EntityManager -> {
            Student student = new Student();
            student.setStudentName(studentName);
            student.setStudentAge(studentAge);
            student.setStudentSocialSecNum(studentSocSecNr);
            student.setTotResult(totResult);
            EntityManager.persist(student);
        });
    }



    // READ - show students

    public void showAllStudents() {
        Main.inTransaction(EntityManager-> {
            TypedQuery<Student> query = EntityManager.createQuery("SELECT s FROM Student s", Student.class);
            List<Student> students = query.getResultList();
            students.forEach(System.out::println);
        });
    }

    //Using dto to show only name and age:
    public void showAllStudentsDto(){
        EntityManager em = JPAUtil.getEntityManager();

        var query = em.createQuery("""
                select new StudentDto(s.studentName, s.studentAge) from Student s
                """, StudentDto.class);
        var students = query.getResultList();

        students.forEach(System.out::println);
        em.close();

    }


    public List<Student> findStudent(String studentName) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Student> query = em.createNamedQuery("student.findByName", Student.class);
            query.setParameter("studentName", studentName);
            return query.getResultList();

        } catch (NoResultException e) {
            System.out.println("No student found with the name: " + studentName);
            return null; // or alternatively, you can throw a custom exception
        }
    }


    public void getAllTestsOfOneStudent(int studentSocSecNum) {

        if (!studentIdExist(studentSocSecNum)){
            System.out.println("Student does not exist in database.");
            return;}

        Main.inTransaction(EntityManager -> {
            Student student = getStudentFromSocSecNum(EntityManager, studentSocSecNum);
            System.out.println(student.getStudentName());
            student.getTests().forEach(test -> System.out.println(test.getTestName()));
            EntityManager.persist(student);
        });
    }


    public void updateStudentInput() {
        var currentName = InputReader.inputString("Enter the name of the student you want to update:");
        var currentSocSecNum = InputReader.inputInt("Enter the social security number of the student:");

        if (!studentIdExist(currentSocSecNum)) {
            System.out.println("Student " + currentName + " does not exist.");
            return;
        }


        var studentName = InputReader.inputString("Enter the students name: ");
        var studentSocSecNr = InputReader.inputInt("Enter the students Social Security Number: ");
        var studentAge = InputReader.inputInt("Enter the students Age: ");
        var totResult = InputReader.inputDouble("Enter the students total Score: ");
        updateStudent(currentName, currentSocSecNum, studentName, studentSocSecNr, studentAge,totResult);
    }

    public void updateStudent(String currentName, int studentSocSecNum, String studentName, int studentSocSecNr, int studentAge, double totResult) {
        Main.inTransaction(entityManager -> {
            TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s WHERE s.studentSocialSecNum = :studentSocialSecNum", Student.class);
            query.setParameter("studentSocialSecNum", studentSocSecNum);
            Student student = query.getSingleResult();
            student.setStudentName(studentName);
            student.setStudentSocialSecNum(studentSocSecNr);
            student.setStudentAge(studentAge);
            student.setTotResult(totResult);
            entityManager.merge(student);
            System.out.println("Student " + currentName + " updated to [name:" + studentName + " social sec num:" + studentSocSecNr + " age:" + studentAge + " total result:" + totResult + "]");
        });
    }

    public void deleteStudent(String studentName) {
        if (studentExist(studentName)) {
            int studentSocSecNum = InputReader.inputInt("enter the students social security number:");
            if (!studentIdExist(studentSocSecNum)){
                System.out.println("Student " + studentName + " does not exist.");
            return;}
            Main.inTransaction(entityManager -> {

                Student student = getStudentFromSocSecNum(entityManager, studentSocSecNum);
                entityManager.remove(student);
                System.out.println("Student " + studentName + " is deleted!");
            });
        } else System.out.println("Student " + studentName + " does not exist.");
    }

    public static boolean studentExist(String studentName) {
        AtomicBoolean exit = new AtomicBoolean(false);
        Main.inTransaction(entityManager -> {
            TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(s) FROM Student s WHERE s.studentName = :studentName", Long.class);
            countQuery.setParameter("studentName", studentName);
            long count = countQuery.getSingleResult();
            exit.set(count > 0);
        });
        return exit.get();
    }

    public static boolean studentIdExist(int studentSocSecNum) {
        AtomicBoolean exit = new AtomicBoolean(false);
        Main.inTransaction(entityManager -> {
            TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(s) FROM Student s WHERE s.studentSocialSecNum = :studentSocSecNum", Long.class);
            countQuery.setParameter("studentSocSecNum", studentSocSecNum);
            long count = countQuery.getSingleResult();
            exit.set(count > 0);
        });
        return exit.get();
    }



    public void studentStatistics() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            // Average student score
            Query avgStudentScoreQuery = em.createQuery("SELECT AVG(s.totResult) FROM Student s");
            Double averageStudentScore = (Double) avgStudentScoreQuery.getSingleResult();
            System.out.println("Average student score: " + averageStudentScore);
            
            // student with the lowest score
            Query lowestScoreStudentQuery = em.createQuery("SELECT s FROM Student s WHERE s.totResult = (SELECT MIN(s2.totResult) FROM Student s2)");
            Student lowestScoreStudent = (Student) lowestScoreStudentQuery.getSingleResult();

            if (lowestScoreStudent != null) {
                System.out.println("Student with lowest score: " + lowestScoreStudent.getStudentName() + " - score: " + lowestScoreStudent.getTotResult());
            } else {
                System.out.println("No student found.");
            }
            // student with the highest score
            Query highestScoreStudentQuery = em.createQuery("SELECT s FROM Student s WHERE s.totResult = (SELECT MAX(s3.totResult) FROM Student s3)");
            Student highestScoreStudent = (Student) highestScoreStudentQuery.getSingleResult();

            if (highestScoreStudent != null) {
                System.out.println("Student with highest score: " + highestScoreStudent.getStudentName() + " - Score: " + highestScoreStudent.getTotResult());
            } else {
                System.out.println("No student found.");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error occurred while generating student statistics: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void studentAvgScorePerTest(String studentName) {
        if (studentExist(studentName)) {
            int studentSocSecNum = InputReader.inputInt("enter the students social security number:");
            if (!studentIdExist(studentSocSecNum)){
                System.out.println("Student " + studentName + " does not exist.");
                return;}
            Main.inTransaction(entityManager -> {

                Student student = getStudentFromSocSecNum(entityManager, studentSocSecNum);
                Set<Test> tests = student.getTests();

                for (Test test : tests) {
                    double testScore = test.getTestScore();
                    String testName = test.getTestName();
                    System.out.println("Average score for " + testName + " is " + testScore);
                }
            });
        } else System.out.println("Student " + studentName + " does not exist.");

    }


    public void avgScorePerTestForStudentsIntervalInput() {
        int minAge = InputReader.inputInt("enter minimum age: ");
        int maxAge = InputReader.inputInt("enter maximum age: ");
        avgScorePerTestForStudentsInterval(minAge, maxAge);
    }

    private void avgScorePerTestForStudentsInterval(int minAge, int maxAge) {

        Main.inTransaction(entityManager -> {
            TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s WHERE s.studentAge >= :minAge AND s.studentAge <= :maxAge", Student.class);
            query.setParameter("minAge", minAge);
            query.setParameter("maxAge", maxAge);

            List<Student> students = query.getResultList();

            double average = 0;
            double sum = 0;
            int count = 0;
            for (Student student : students) {
                Set<Test> tests = student.getTests();
                for (Test test : tests) {
                    double testScore = test.getTestScore();

                    sum += testScore;
                    count++;
                }
            }
            average = sum / count;
            System.out.println("Average score for students age " + minAge + " to " + maxAge + " is: " + average);
        });
    }

    private static Student getStudentFromSocSecNum(EntityManager entityManager, int studentSocSecNum) {
        TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s WHERE s.studentSocialSecNum = :studentSocSecNum", Student.class);
        query.setParameter("studentSocSecNum", studentSocSecNum);

        return query.getSingleResult();
    }

}
