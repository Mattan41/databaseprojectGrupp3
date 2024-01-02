package com.example;

import com.example.dao.*;
import com.example.util.InputReader;
import com.example.util.Menu;
import jakarta.persistence.*;

import java.util.Scanner;
import java.util.function.Consumer;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        var planetDao = new PlanetDao();
        var moonDao = new MoonDao();
        var solarSystemDao = new SolarSystemDao();
        var studentDao = new StudentDao();
        var testsDao = new TestDao();

        var mainMenu = new Menu("Main Menu!");
        var moonMenu = new Menu("Moon");
        var planetsMenu = new Menu("Planets");
        var solarSystemMenu = new Menu("Solar Systems");
        var studentMenu = new Menu("Students");
        var viewStudentMenu = new Menu("View students");
        var statisticsMenu = new Menu("Statistics");
        var testsMenu = new Menu("Tests");

        planetsMenu.addMenuItem("Show all planets", planetDao::showAllPlanets);
        planetsMenu.addMenuItem("Find a planet", () -> System.out.println(planetDao.findPlanet(InputReader.inputString("Enter planet name: "))));
        planetsMenu.addMenuItem("Insert planet", planetDao::insertPlanetInput);
        planetsMenu.addMenuItem("Show the moons of a planet", () -> planetDao.getMoonsOfPlanet(InputReader.inputString("Enter planet name:")));
        planetsMenu.addMenuItem("Delete planet", () -> planetDao.deletePlanet(InputReader.inputString("Enter the name of planet to delete: ")));
        planetsMenu.addMenuItem("Update planet", planetDao::updatePlanetInput);
        planetsMenu.addMenuItem("Statistics", planetDao::planetStatistics);

        moonMenu.addMenuItem("Show all moons", moonDao::showAllMoons);
        moonMenu.addMenuItem("Show one moon", () -> System.out.println(moonDao.findAMoon(InputReader.inputString("Enter the moon name: "))));
        moonMenu.addMenuItem("Insert moon", moonDao::insertMoonInput);
        moonMenu.addMenuItem("Update moon", moonDao::updateMoonInput);
        moonMenu.addMenuItem("Delete moon", () -> moonDao.deleteMoon(InputReader.inputString("Enter the name of the moon to delete:")));
        moonMenu.addMenuItem("Statistics", moonDao::moonStatistics);

        solarSystemMenu.addMenuItem("Show all solar systems", solarSystemDao::showAllSolarSystems);
        solarSystemMenu.addMenuItem("Insert solar systems", solarSystemDao::insertSolarSystemInput);
        solarSystemMenu.addMenuItem("Count solar systems", () -> System.out.println("The number of solar systems are " + solarSystemDao.countSolarSystems()));


        studentMenu.addMenuItem("View students", viewStudentMenu::displayMenu);
        studentMenu.addMenuItem("Insert students", studentDao::insertStudentInput);
        studentMenu.addMenuItem("Update student", studentDao::updateStudentInput);
        studentMenu.addMenuItem("Delete student", () -> studentDao.deleteStudent(InputReader.inputInt("enter the students social security number:")));
        studentMenu.addMenuItem("Statistics", statisticsMenu::displayMenu);

        viewStudentMenu.addMenuItem("Show all students", studentDao::showAllStudents);
        viewStudentMenu.addMenuItem("Find student", () -> System.out.println(studentDao.findStudent(InputReader.inputString("Enter the students name: "))));
        viewStudentMenu.addMenuItem("Show all tests from one Student", () -> studentDao.getAllTestsOfOneStudent(InputReader.inputInt("Enter the Students Social security number:")));

        statisticsMenu.addMenuItem("Statistics", studentDao::studentStatistics);
        statisticsMenu.addMenuItem("Show average score per test for one student", () -> studentDao.studentAvgScorePerTestInput(InputReader.inputInt("Enter the students social security number: ")));
        statisticsMenu.addMenuItem("Show average score for students at age interval: ", studentDao::avgScorePerTestForStudentsIntervalInput);



        testsMenu.addMenuItem("Show all tests", testsDao::showAllTests);
        testsMenu.addMenuItem("Add test", testsDao::insertTestInput);

        mainMenu.addMenuItem("Planet", planetsMenu::displayMenu);
        mainMenu.addMenuItem("Moon", moonMenu::displayMenu);
        mainMenu.addMenuItem("Solar System", solarSystemMenu::displayMenu);
        mainMenu.addMenuItem("Students", studentMenu::displayMenu);
        mainMenu.addMenuItem("Tests", testsMenu::displayMenu);

        mainMenu.displayMenu();
    }

    public static void inTransaction(Consumer<EntityManager> work) {
        try (EntityManager entityManager = JPAUtil.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                work.accept(entityManager);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }
}
