package com.example.dao;

import com.example.JPAUtil;
import com.example.Main;
import com.example.entities.Moon;
import com.example.entities.Planet;
import com.example.util.InputReader;
import jakarta.persistence.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MoonDao {

    // Visa alla månar
    public void showAllMoons() {
        Main.inTransaction(EntityManager -> {
            try {
                TypedQuery<Moon> query = EntityManager.createQuery("SELECT m FROM Moon m", Moon.class);
                List<Moon> moons = query.getResultList();
                moons.forEach(System.out::println);
            } catch (Exception e) {
                throw e;
            }
        });
    }

    public void insertMoonInput() {
        var planetId = InputReader.inputInt("Enter the planet id that the new moon belongs to: ");

        if (!planetExist(planetId)) {
            System.out.println("Planet with ID " + planetId + " does not exist in the database.");
            return;
        }

        var moonName = InputReader.inputString("Enter moon name to add: ");
        Double moonSize = InputReader.inputDouble("Enter moon size: ");

        if (moonExist(moonName)) {
            System.out.println(moonName + " already exists i database.");
            return;
        }
        insertMoon(moonName, moonSize, planetId);
    }

    private boolean planetExist(int planetId) {
        AtomicBoolean exists = new AtomicBoolean(false);
        Main.inTransaction(entityManager -> {
            Planet planet = entityManager.find(Planet.class, planetId);
            exists.set(planet != null);
        });
        return exists.get();
    }

    private static void insertMoon(String moonName, Double moonSize, int planetId) {
        Main.inTransaction(entityManager -> {
            Moon moon = new Moon();
            moon.setName(moonName);
            moon.setSize(moonSize);
            moon.setPlanetId(planetId);
            entityManager.persist(moon);
            System.out.println("Moon " + moonName + " added to the database!");
        });
    }

    public static boolean moonExist(String moonName) {
        AtomicBoolean exit = new AtomicBoolean();
        Main.inTransaction(entityManager -> {
            TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(m) FROM Moon m WHERE m.name = :moonName", Long.class);
            countQuery.setParameter("moonName", moonName);
            long count = countQuery.getSingleResult();
            exit.set(count > 0);
        });
        return exit.get();
    }

    public void updateMoonInput() {
        var currentName = InputReader.inputString("Enter the name of the moon you want to update:");
        if (!moonExist(currentName)) {
            System.out.println("Moon " + currentName + " does not exist.");
            return;
        }
        var newName = InputReader.inputString("Enter the new name:");
        var newSize = InputReader.inputDouble("Enter the new size:");
        var planetId = InputReader.inputInt("Enter planet id:");
        updateMoon(currentName, newName, newSize, planetId);
    }

    public void updateMoon(String currentName, String newName, double newSize, int planetId) {
        Main.inTransaction(entityManager -> {
            try {
                TypedQuery<Moon> query = entityManager.createQuery("SELECT m FROM Moon m WHERE m.name = :name", Moon.class);
                query.setParameter("name", currentName);
                Moon moon = query.getSingleResult();
                moon.setName(newName);
                moon.setSize(newSize);
                moon.setPlanetId(planetId);
                entityManager.merge(moon);
                System.out.println("Moon " + currentName + " updated to [name:" + newName + " size:" + newSize + "]");
            } catch (Exception e) {
                throw e;
            }
        });
    }

    public void deleteMoon(String moonName) {
        if (moonExist(moonName)) {
            Main.inTransaction(entityManager -> {
                try {
                    TypedQuery<Moon> query = entityManager.createQuery("SELECT m FROM Moon m WHERE m.name = :name", Moon.class);
                    query.setParameter("name", moonName);
                    Moon moon = query.getSingleResult();
                    entityManager.remove(moon);
                    System.out.println("The moon " + moonName + " is deleted!");
                } catch (Exception e) {
                    throw e;
                }
            });
        } else System.out.println("Moon " + moonName + " does not exist.");
    }

    public void moonStatistics() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();
            // Average moon size
            Query avgMoonSizeQuery = em.createQuery("SELECT AVG(m.size) FROM Moon m");
            Double averageMoonSize = (Double) avgMoonSizeQuery.getSingleResult();
            System.out.println("Average Moon Size: " + averageMoonSize);
            // Smallest moon
            Query smallestMoonQuery = em.createQuery("SELECT m FROM Moon m WHERE m.size = (SELECT MIN(m2.size) FROM Moon m2)");
            Moon smallestMoon = (Moon) smallestMoonQuery.getSingleResult();

            if (smallestMoon != null) {
                System.out.println("Smallest moon: " + smallestMoon.getName() + " - Size: " + smallestMoon.getSize());
            } else {
                System.out.println("No moon found.");
            }
            // Biggest moon
            Query biggestMoonQuery = em.createQuery("SELECT m FROM Moon m WHERE m.size = (SELECT MAX(m3.size) FROM Moon m3)");
            Moon biggestMoon = (Moon) biggestMoonQuery.getSingleResult();

            if (biggestMoon != null) {
                System.out.println("Biggest moon: " + biggestMoon.getName() + " - Size: " + biggestMoon.getSize());
            } else {
                System.out.println("No moon found.");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error occurred while generating moon statistics: " + e.getMessage());
        } finally {
            em.close();
        }
    }


    // Find one moon and return it as a record. Here in this console app I have overridden the toString to make a nice print to the console.
    public MoonRecord findAMoon(String moonName) {
        AtomicReference<MoonRecord> moonRecordRef = new AtomicReference<>();
        Main.inTransaction(entityManager -> {
            TypedQuery<Moon> query = entityManager.createQuery("SELECT m FROM Moon m WHERE m.name = :moonName", Moon.class);
            query.setParameter("moonName", moonName);
            Moon moon = query.getSingleResult();

            if (moon != null) {
                MoonRecord moonRecord = new MoonRecord(moon.getName(), moon.getSize(), moon.getPlanetId());
                moonRecordRef.set(moonRecord);
            }
        });

        return moonRecordRef.get();
    }

    // Record for displaying a moon
    public record MoonRecord(String name, Double size, int planetId) {
        @Override
        public String toString() {
            return "Moon: name= %s, size= %s, planetId= %d".formatted(name, size, planetId);
        }
    }
}