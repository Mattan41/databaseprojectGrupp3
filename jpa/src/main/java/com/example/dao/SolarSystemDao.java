package com.example.dao;

import com.example.JPAUtil;
import com.example.entities.SolarSystem;
import com.example.util.InputReader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class SolarSystemDao {
    // skapar solsystem

    public void insertSolarSystem(){
        EntityManager em = JPAUtil.getEntityManager();

        var solarSystemName = InputReader.inputString("Enter solar system: ");
        var galaxyName = InputReader.inputString("Enter galaxy: ");

        try {
            em.getTransaction().begin();

            SolarSystem solarSystem = new SolarSystem();
            solarSystem.setSolarSystemName(solarSystemName);
            solarSystem.setGalaxyName(galaxyName);

            em.persist(solarSystem);
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

    // Visa alla solsystem
    public void showAllSolarSystems() {
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

    private static void handleException(Exception e) {
        e.printStackTrace();
    }
}
