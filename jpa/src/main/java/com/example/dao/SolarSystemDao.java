package com.example.dao;

import com.example.JPAUtil;
import com.example.Main;
import com.example.entities.Moon;
import com.example.entities.SolarSystem;
import com.example.util.InputReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SolarSystemDao {


    public void showAllSolarSystems() {
        Main.inTransaction(EntityManager -> {
            try {
                TypedQuery<SolarSystem> query = EntityManager.createQuery("SELECT s FROM SolarSystem s", SolarSystem.class);
                List<SolarSystem> solarSystems = query.getResultList();
                solarSystems.forEach(System.out::println);
            }catch (Exception e) {throw e;}
        });
    }

    public long countSolarSystems() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Query query = em.createQuery("SELECT COUNT(s) FROM SolarSystem s");
            return (long) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return 0;
        } finally {
            em.close();
        }
    }

    public void insertSolarSystemInput() {
        var solarSystemName = InputReader.inputString("Enter solar system to add: ");
        if (solarSystemExist(solarSystemName)) {
            System.out.println(solarSystemName + " already in database");
            return;
        }
        var galaxyName = InputReader.inputString("Enter galaxy name");

        insertSolarSystem(solarSystemName, galaxyName);
    }

    private static void insertSolarSystem(String solarSystemName, String galaxyName) {
        Main.inTransaction(entityManager -> {
            try {
                SolarSystem solarSystem = new SolarSystem();
                solarSystem.setSolarSystemName(solarSystemName);
                solarSystem.setGalaxyName(galaxyName);
                entityManager.persist(solarSystem);
                System.out.println("Solar system " + solarSystem + " added to the database!");
            }catch (Exception e) {throw e;}
        });
    }

    public void updateSolarSystemInput() {
        var currentName = InputReader.inputString("Enter the name of the solar system you want to update:");
        if (!solarSystemExist(currentName)) {
            System.out.println("Solar system " + currentName + " does not exist.");
            return;
        }
        var newName = InputReader.inputString("Enter the new name:");
        var galaxyName = InputReader.inputString("Enter the galaxy:");
        updateSolarSystem(currentName, newName, galaxyName);
    }

    public void updateSolarSystem(String currentName, String newName, String galaxyName) {
        Main.inTransaction(entityManager -> {
            try {
                TypedQuery<SolarSystem> query = entityManager.createQuery("SELECT s FROM SolarSystem s WHERE s.solarSystemName = :solarSystemName", SolarSystem.class);
                query.setParameter("solarSystemName", currentName);
                SolarSystem solarSystem = query.getSingleResult();
                solarSystem.setSolarSystemName(newName);
                solarSystem.setGalaxyName(galaxyName);
                entityManager.merge(solarSystem);
                System.out.println("Solar system " + currentName + " updated to [name:" + newName + " galaxy:" + galaxyName + "]");
            } catch (Exception e) {throw e;}
        });
    }
    public void deleteSolarSystem(String solarSystemName) {
        if (solarSystemExist(solarSystemName)) {
            Main.inTransaction(entityManager -> {
                try {
                    TypedQuery<SolarSystem> query = entityManager.createQuery("SELECT s FROM SolarSystem s WHERE s.solarSystemName = :solarSystemName", SolarSystem.class);
                    query.setParameter("solarSystemName", solarSystemName);
                    SolarSystem solarSystem = query.getSingleResult();
                    entityManager.remove(solarSystem);
                    System.out.println("The solar system " + solarSystem + " is deleted!");
                } catch (Exception e) {throw e;}
            });
        } else System.out.println("Solar system " + solarSystemName + " does not exist.");
    }

    public static boolean solarSystemExist(String solarSystemName) {
        AtomicBoolean exit = new AtomicBoolean(false);
        Main.inTransaction(entityManager -> {
            TypedQuery<Long> countQuery = entityManager.createQuery(
                    "SELECT COUNT(s) FROM SolarSystem s WHERE s.solarSystemName = :solarSystemName", Long.class);
            countQuery.setParameter("solarSystemName", solarSystemName);
            long count = countQuery.getSingleResult();
            exit.set(count > 0);
        });
        return exit.get();
    }
}
