package com.example.dao;

import com.example.JPAUtil;
import com.example.entities.Moon;
import com.example.entities.Planet;
import com.example.util.InputReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.function.Consumer;

public class MoonDao {

    // Visa alla månar
    public void showAllMoons() {
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

    private static void handleException(Exception e) {
        e.printStackTrace();
    }

    public void insertMoon() {

        var moonName = InputReader.inputString("Enter moon to add: ");
        if (moonExist(moonName)) {
            System.out.println(moonName + " already in database");
            return;
        }

        Double moonSize = InputReader.inputDouble("Enter moon size");
        if (moonSize <= 0) {
            System.out.println("Invalid input.");
            return;
        }

        var planetId = InputReader.inputInt("Enter moonId: ");

        inTransaction(entityManager -> {
            try {
                Moon moon = new Moon();
                moon.setName(moonName);
                moon.setSize(moonSize);
                moon.setPlanetId(planetId);
                entityManager.persist(moon);
                System.out.println("Moon " + moonName + " added to the database!");
            }catch (Exception e) {throw e;}
        });
    }


    public static boolean moonExist(String moonName) {
            EntityManager em = JPAUtil.getEntityManager();
            TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(m) FROM Moon m WHERE m.name = :moonName", Long.class);
            countQuery.setParameter("moonName", moonName);
            long count = countQuery.getSingleResult();
        return count > 0;
    }

    public void updateMoonInput() {
        var currentName = InputReader.inputString("Enter the name of the moon you want to update:");
        var newName = InputReader.inputString("Enter the new name:");
        var newSize = InputReader.inputDouble("Enter the new size:");
        var planetId = InputReader.inputInt("Enter moon id:");
        if (moonExist(currentName)) updateMoon(currentName, newName, newSize, planetId);
        else System.out.println("Moon " + currentName + " does not exist.");
    }

    public void updateMoon(String currentName, String newName, double newSize, int planetId) {
        inTransaction(entityManager -> {
            try {
                TypedQuery<Moon> query = entityManager.createQuery("SELECT m FROM Moon m WHERE m.name = :name", Moon.class);
                query.setParameter("name", currentName);
                Moon moon = query.getSingleResult();
                moon.setName(newName);
                moon.setSize(newSize);
                moon.setPlanetId(planetId);
                entityManager.merge(moon);
                System.out.println("Moon " + currentName + " updated to [name:" + newName + " size:" + newSize + "]");
            } catch (Exception e) {throw e;}
        });
    }

    public void deleteMoon(String moonName) {
        if (moonExist(moonName)) {
            inTransaction(entityManager -> {
                try {
                    TypedQuery<Moon> query = entityManager.createQuery("SELECT m FROM Moon m WHERE m.name = :name", Moon.class);
                    query.setParameter("name", moonName);
                    Moon moon = query.getSingleResult();
                    entityManager.remove(moon);
                    System.out.println("The moon " + moonName + " is deleted!");
                } catch (Exception e) {throw e;}
            });
        } else System.out.println("Moon " + moonName + " does not exist.");
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