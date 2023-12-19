package com.example.dao;

import com.example.JPAUtil;
import com.example.entities.Planet;
import com.example.util.InputReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Scanner;

public class PlanetDao {
//    EntityManager em = JPAUtil.getEntityManager();

    public void showAllPlanets() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Planet> query = em.createQuery("SELECT p FROM Planet p", Planet.class);
        List<Planet> planets = query.getResultList();
        planets.forEach(System.out::println);
        em.close();
    }

    public void createPlanet() {
        EntityManager em = JPAUtil.getEntityManager();

        var planetName = InputReader.inputString("Enter planet name: ");
        var planetSize = InputReader.inputInt("Enter size: ");
        var planetType = InputReader.inputString("Enter planet type: ");
        var solarSystemId = InputReader.inputInt("Enter solar system Id: ");

        // Validate user input
        if (planetName == null || planetName.isEmpty() || planetSize <= 0 || planetType == null || planetType.isEmpty()) {
            System.out.println("Invalid input.");
            return;
        }

        try {
            em.getTransaction().begin();

            Planet planet = new Planet();
            planet.setPlanetName(planetName);
            planet.setPlanetSize(planetSize);
            planet.setPlanetType(planetType);
            planet.setSolarSystemId(solarSystemId);

            em.persist(planet);
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

    public void updatePlanet() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the planet you want to update:");
        String currentName = scanner.nextLine();
        System.out.println("Enter the new name:");
        String newName = scanner.nextLine();
        System.out.println("Enter the new size:");
        int newSize = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter the new type:");
        String newType = scanner.nextLine();

        updatePlanetName(currentName, newName, newSize, newType);

    }

    public void updatePlanetName(String currentName, String newName, int newSize, String newType) {

        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        TypedQuery<Planet> query = em.createQuery("SELECT p FROM Planet p WHERE p.planetName = :planetName", Planet.class);
        query.setParameter("planetName", currentName);
        try {
            Planet planet = query.getSingleResult();
            planet.setPlanetName(newName);
            planet.setPlanetSize(newSize);
            planet.setPlanetType(newType);
            em.merge(planet);
            em.getTransaction().commit();
            System.out.println("Planet " + currentName + " updated to [name:" + newName + " size:" + newSize + " type:" + newType + "]");
        } catch (NoResultException e) {
            System.out.println("Planet " + currentName + " not found");
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
        em.close();
    }
}
