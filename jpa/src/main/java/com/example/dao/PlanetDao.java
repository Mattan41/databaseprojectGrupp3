package com.example.dao;

import com.example.JPAUtil;
import com.example.entities.Planet;
import com.example.util.InputReader;
import jakarta.persistence.*;

import java.util.List;

public class PlanetDao {
//    EntityManager em = JPAUtil.getEntityManager();

    public void showAllPlanets() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Planet> query = em.createQuery("SELECT p FROM Planet p", Planet.class);
        List<Planet> planets = query.getResultList();
        planets.forEach(System.out::println);
        em.close();
    }

    public Planet findPlanet(String planetName) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Planet> query = em.createQuery("SELECT p FROM Planet p WHERE p.planetName = :planetName", Planet.class);
            query.setParameter("planetName", planetName);

            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No planet found with the name: " + planetName);
            return null; // or alternatively, you can throw a custom exception
        } finally {
            em.close();
        }
    }

    public void insertPlanetInput() {

        var planetName = InputReader.inputString("Enter planet name: ");
        if (planetExist(planetName)) {
            System.out.println(planetName + " already in database");
            return;
        }
        var planetSize = InputReader.inputInt("Enter size: ");
        var planetType = InputReader.inputString("Enter planet type: ");
        var solarSystemId = InputReader.inputInt("Enter solar system Id: ");

        insertPlanet(planetName, planetSize, planetType, solarSystemId);
    }

    private static void insertPlanet(String planetName, int planetSize, String planetType, int solarSystemId) {
        EntityManager em = JPAUtil.getEntityManager();

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

    public static boolean planetExist(String currentName) {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(p) FROM Planet p WHERE p.planetName = :planetName", Long.class);
        countQuery.setParameter("planetName", currentName);
        long count = countQuery.getSingleResult();
        em.close();
        return count > 0;
    }

    public void getMoonsOfPlanet(String planetName) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Moon> query = em.createQuery(
                    "SELECT m FROM Moon m INNER JOIN Planet p ON m.planetId = p.id WHERE p.planetName = :planetName",
                    Moon.class
            );
            query.setParameter("planetName", planetName);
            List<Moon> moons = query.getResultList();
            moons.forEach(System.out::println);
        } finally {
            em.close();
        }
    }
}



