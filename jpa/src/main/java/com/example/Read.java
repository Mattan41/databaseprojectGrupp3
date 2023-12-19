package com.example;

import com.example.JPAUtil;
import com.example.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Scanner;

public class Read {

    // Visa alla planeter
    public static void showAllPlanets() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Planet> query = em.createQuery("SELECT p FROM Planet p", Planet.class);
            List<Planet> planets = query.getResultList();
            planets.forEach(System.out::println);
        } catch (Exception e) {
            handleException(e);
        } finally {
            em.close();
        }
    }

    // Visa alla månar
    public static void showAllMoons() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Moon> query = em.createQuery("SELECT m FROM Moon m", Moon.class);
            List<Moon> moons = query.getResultList();
            moons.forEach(System.out::println);
        } catch (Exception e) {
            handleException(e);
        } finally {
            em.close();
        }
    }

    // Visa alla solar system
    public static void showAllSolarSystems() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<SolarSystem> query = em.createQuery("SELECT s FROM SolarSystem s", SolarSystem.class);
            List<SolarSystem> solarSystems = query.getResultList();
            solarSystems.forEach(System.out::println);
        } catch (Exception e) {
            handleException(e);
        } finally {
            em.close();
        }
    }

    // Visa alla studenter
    public static void showAllStudents() {
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

    // Visa alla tester
    public static void showAllTests() {
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


    static void readStudent(EntityManager em) {
        System.out.print("Enter search term: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        // Validate user input
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid input.");
            return;
        }

        TypedQuery<Student> query = em.createQuery("SELECT c FROM Student c WHERE c.studentName = :name", Student.class);
        query.setParameter("name", name);
        List<Student> students = query.getResultList();
        students.forEach(System.out::println);

        em.close();
    }
}
