import com.example.entities.Planet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.Scanner;

public class Update {
    static EntityManager em = JPAUtil.getEntityManager();

    private static Scanner scanner = new Scanner(System.in);

    public static void updatePlanet() {
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

    public static void updatePlanetName(String currentName, String newName, int newSize, String newType) {
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
