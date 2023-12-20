package com.example.dao;

import com.example.JPAUtil;
import com.example.entities.Moon;
import com.example.entities.SolarSystem;
import com.example.util.InputReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.function.Consumer;

public class SolarSystemDao {


    public void showAllSolarSystems() {
        inTransaction(EntityManager -> {
            try {
                TypedQuery<SolarSystem> query = EntityManager.createQuery("SELECT s FROM SolarSystem s", SolarSystem.class);
                List<SolarSystem> solarSystems = query.getResultList();
                solarSystems.forEach(System.out::println);
            }catch (Exception e) {throw e;}
        });
    }

    private static void handleException(Exception e) {
        e.printStackTrace();
    }


    public void insertSolarSystem() {
        var solarSystemName = InputReader.inputString("Enter solar system to add: ");
        if (solarSystemExist(solarSystemName)) {
            System.out.println(solarSystemName + " already in database");
            return;
        }
        var galaxyName = InputReader.inputString("Enter galaxy name");
        inTransaction(entityManager -> {
            try {
                SolarSystem solarSystem = new SolarSystem();
                solarSystem.setSolarSystemName(solarSystemName);
                solarSystem.setGalaxyName(galaxyName);
                entityManager.persist(solarSystem);
                System.out.println("'Solar system " + solarSystem + " added to the database!");
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
        inTransaction(entityManager -> {
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
            inTransaction(entityManager -> {
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
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(s) FROM SolarSystem s WHERE s.solarSystemName = :solarSystemName", Long.class);
        countQuery.setParameter("solarSystemName", solarSystemName);
        long count = countQuery.getSingleResult();
        return count > 0;
    }


    static void inTransaction(Consumer<EntityManager> work) {
        try (EntityManager entityManager = JPAUtil.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                work.accept(entityManager);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) transaction.rollback();
                throw e;
            }finally {
                entityManager.close();
            }
        }
    }
}
