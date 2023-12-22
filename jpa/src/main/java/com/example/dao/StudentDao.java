package com.example.dao;

import com.example.JPAUtil;
import com.example.Main;
import com.example.entities.Student;
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
        var currentSocSecNum = InputReader.inputInt("Enter the social security number of the student:");

        if (!studentIdExist(currentSocSecNum)) {
            System.out.println("Student does not exist in database.");
            return;
        }

        var studentName = InputReader.inputString("Enter the students name: ");
        var studentSocSecNr = InputReader.inputInt("Enter the students Social Security Number: ");
        var studentAge = InputReader.inputInt("Enter the students Age: ");
        var totResult = InputReader.inputDouble("Enter the students total Score: ");
        updateStudent(currentSocSecNum, studentName, studentSocSecNr, studentAge,totResult);
    }

    public void updateStudent(int studentSocSecNum, String studentName, int studentSocSecNr, int studentAge, double totResult) {
        Main.inTransaction(entityManager -> {
            TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s WHERE s.studentSocialSecNum = :studentSocialSecNum", Student.class);
            query.setParameter("studentSocialSecNum", studentSocSecNum);
            Student student = query.getSingleResult();
            String formerName = student.getStudentName();
            student.setStudentName(studentName);
            student.setStudentSocialSecNum(studentSocSecNr);
            student.setStudentAge(studentAge);
            student.setTotResult(totResult);
            entityManager.merge(student);
            System.out.println("Student " + formerName + " updated to [name:" + studentName + ", Social security number:" + studentSocSecNr + ", Age:" + studentAge + ", Total result:" + totResult + "]");
        });
    }

    public void deleteStudent(int studentSocSecNum) {

            if (!studentIdExist(studentSocSecNum)){
                System.out.println("Student does not exist in database.");
            return;}
            Main.inTransaction(entityManager -> {

                Student student = getStudentFromSocSecNum(entityManager, studentSocSecNum);
                entityManager.remove(student);
                System.out.println("Student " + student.getStudentName() + " is deleted!");
            });

    }

    //Statistics
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

    public void studentAvgScorePerTestInput(int studentSocSecNum) {
        if (!studentIdExist(studentSocSecNum)){
                System.out.println("Student does not exist in database.");
                return;}
            studentAvgScorePerTest(studentSocSecNum);


    }
    private static void studentAvgScorePerTest(int studentSocSecNum) {
        Main.inTransaction(entityManager -> {

            Student student = getStudentFromSocSecNum(entityManager, studentSocSecNum);
            double averageTestScore = getAverageTestScore(student);

            System.out.println("Average score per test for " + student.getStudentName() + " is: " + averageTestScore );
        });
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

            double averageScoreTotal = getAverageScoreTotal(students);

            System.out.println("Average score for students age " + minAge + " - " + maxAge + " is: " + averageScoreTotal);
        });
    }

    private static double getAverageScoreTotal(List<Student> students) {
        double averageScoreForStudents = 0;
        int countStudents = 0;
        for (Student student : students) {

            double average = getAverageTestScore(student);
            averageScoreForStudents += average;
            countStudents++;
        }

        return averageScoreForStudents / countStudents;
    }
    private static double getAverageTestScore(Student student) {
        Set<Test> tests = student.getTests();
        int count = 0;
        double testScore = 0;

        for (Test test : tests) {
            testScore += test.getTestScore();
            count++;

        }
        return testScore / count;
    }

    //misc
    public static boolean studentNameExist(String studentName) {
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
    private static Student getStudentFromSocSecNum(EntityManager entityManager, int studentSocSecNum) {
        TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s WHERE s.studentSocialSecNum = :studentSocSecNum", Student.class);
        query.setParameter("studentSocSecNum", studentSocSecNum);

        return query.getSingleResult();
    }
}
