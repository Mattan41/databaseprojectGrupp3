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
        var testsMenu = new Menu("Tests");

        planetsMenu.addMenuItem("Show Planets", planetDao::showAllPlanets);
        planetsMenu.addMenuItem("Find a planet", () -> System.out.println(planetDao.findPlanet(InputReader.inputString("Enter planet name: "))));
        planetsMenu.addMenuItem("Insert planet", planetDao::insertPlanetInput);
        planetsMenu.addMenuItem("Show the moons of a planet", planetDao::showMoonsForPlanet);
        planetsMenu.addMenuItem("Delete planet", () -> planetDao.deletePlanet(InputReader.inputString("Enter the name of planet to delete: ")));
        planetsMenu.addMenuItem("Update planet", planetDao::updatePlanetInput);

        moonMenu.addMenuItem("Show moons", moonDao::showAllMoons);0
        moonMenu.addMenuItem("Insert moon", moonDao::insertMoon);
        moonMenu.addMenuItem("Update moon", moonDao::updateMoonInput);
        moonMenu.addMenuItem("Delete moon", () -> moonDao.deleteMoon(InputReader.inputString("Enter the name of the moon to delete:")));

        solarSystemMenu.addMenuItem("Show solar systems", solarSystemDao::showAllSolarSystems);

        studentMenu.addMenuItem("Show students", studentDao::showAllStudents);

        testsMenu.addMenuItem("Show tests", testsDao::showAllTests);

        mainMenu.addMenuItem("Planet", planetsMenu::displayMenu);
        mainMenu.addMenuItem("Moon", moonMenu::displayMenu);
        mainMenu.addMenuItem("Solar System", solarSystemMenu::displayMenu);
        mainMenu.addMenuItem("Students", studentMenu::displayMenu);
        mainMenu.addMenuItem("Tests", testsMenu::displayMenu);

        mainMenu.displayMenu();
    }

    static void inTransaction(Consumer<EntityManager> work) {
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
//    public static void mainMenu(){
//        var mainMenu = new Menu("com.example.Main Menu!");
//        mainMenu.addMenuItem("1 - Planet", com.example.Main::planetsMenu);
//        mainMenu.addMenuItem("2 - Moon", com.example.Main::moonMenu);
//        mainMenu.addMenuItem("3 - Solar system", com.example.Main::solarSystemMenu);
//        mainMenu.addMenuItem("4 - Student", com.example.Main::studentMenu);
//        mainMenu.addMenuItem("5 - Test", com.example.Main::testMenu);
//        mainMenu.displayMenu();
//    }
//
//    public static void planetsMenu(){
//        var planetMenu = new Menu("Planet Menu!");
//        planetMenu.addMenuItem("1 - Show all planets", Read::showAllPlanets);
//        planetMenu.addMenuItem("2 - Insert a new planet", Create::createPlanet);
//        planetMenu.addMenuItem("3 - Update a planet", Update::updatePlanet);
//        planetMenu.addMenuItem("4 - Delete a planet", () -> System.out.println("delete"));
//        planetMenu.displayMenu();
//    }
//    public static void moonMenu(){
//        var moonMenu = new Menu("Moon Menu!");
//        moonMenu.addMenuItem("1 - Show all moons", Read::showAllMoons);
//        moonMenu.addMenuItem("2 - Insert a new moon", () -> System.out.println("insert!"));
//        moonMenu.addMenuItem("3 - Update a moon", () -> System.out.println("update!"));
//        moonMenu.addMenuItem("4 - Delete a moon", () -> System.out.println("delete"));
//        moonMenu.displayMenu();
//    }
//    public static void solarSystemMenu(){
//        var solarSystemMenu = new Menu("Solar system Menu!");
//        solarSystemMenu.addMenuItem("1 - Show all solar systems", Read::showAllSolarSystems);
//        solarSystemMenu.addMenuItem("2 - Insert a new solar system", () -> System.out.println("insert!"));
//        solarSystemMenu.addMenuItem("3 - Update a solar system", () -> System.out.println("update!"));
//        solarSystemMenu.addMenuItem("4 - Delete a solar system", () -> System.out.println("delete"));
//        solarSystemMenu.displayMenu();
//    }
//    public static void studentMenu(){
//        var studentMenu = new Menu("Student Menu!");
//        studentMenu.addMenuItem("1 - Show all Students", Read::showAllStudents);
//        studentMenu.addMenuItem("2 - Insert a new Student", () -> System.out.println("insert!"));
//        studentMenu.addMenuItem("3 - Update a Student", () -> System.out.println("update!"));
//        studentMenu.addMenuItem("4 - Delete a Student", () -> System.out.println("delete"));
//        studentMenu.displayMenu();
//    }
//
//    public static void testMenu(){
//        var testMenu = new Menu("Test Menu!");
//        testMenu.addMenuItem("1 - Show all tests", Read::showAllTests);
//        testMenu.addMenuItem("2 - Insert a new test", () -> System.out.println("insert!"));
//        testMenu.addMenuItem("3 - Update a test", () -> System.out.println("update!"));
//        testMenu.addMenuItem("4 - Delete a test", () -> System.out.println("delete"));
//        testMenu.displayMenu();
//    }


}
