package com.example.dao;

import com.example.JPAUtil;
import com.example.entities.Planet;
import com.example.util.InputReader;
import jakarta.persistence.*;

import java.math.BigDecimal;
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

    public void insertPlanet() {
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

    public void updatePlanetInput() {
        var currentName = InputReader.inputString("Enter the name of the planet you want to update:");
        var newName = InputReader.inputString("Enter the new name:");
        var newSize = InputReader.inputInt("Enter the new size:");
        var newType = InputReader.inputString("Enter the new type:");
        if (planetExist(currentName)) updatePlanet(currentName, newName, newSize, newType);
        else System.out.println("Planet " + currentName + " does not exist.");
    }

    public void updatePlanet(String currentName, String newName, int newSize, String newType) {
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
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
        em.close();
    }

    public void deletePlanet(String planetName) {

        EntityTransaction transaction = null;
        try (EntityManager entityManager = JPAUtil.getEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();

            TypedQuery<Planet> query = entityManager.createNamedQuery("Planet.findByName", Planet.class);
            query.setParameter("planetName", planetName);

            Planet planet = query.getSingleResult();
            entityManager.remove(planet);
            transaction.commit();

            System.out.println("The planet " + planetName + " is deleted!");
        } catch (NoResultException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("Planet not found with name: " + planetName);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    public static boolean planetExist(String currentName) {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(p) FROM Planet p WHERE p.planetName = :planetName", Long.class);
        countQuery.setParameter("planetName", currentName);
        long count = countQuery.getSingleResult();
        em.close();
        return count > 0;
    }

    public void planetStatistics() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Query avgSizeQuery = em.createQuery("SELECT AVG(p.planetSize) FROM Planet p");
            Double averageSize = (Double) avgSizeQuery.getSingleResult();
            System.out.println("Average Planet Size: " + averageSize);

            Query smallestQuery = em.createQuery("SELECT p FROM Planet p WHERE p.planetSize = (SELECT MIN(p2.planetSize) FROM Planet p2)");
            Planet smallestPlanet = (Planet) smallestQuery.getSingleResult();

            if (smallestPlanet != null) {
                System.out.println("Smallest Planet: " + smallestPlanet.getPlanetName() + " - Size: " + smallestPlanet.getPlanetSize());
            } else {
                System.out.println("No planet found.");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error occurred while generating planet statistics: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void showPlanetsMoons() {

    }
}


