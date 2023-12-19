import com.example.entities.Planet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.Scanner;

public class Update {

    private static Scanner scanner = new Scanner(System.in);

    public static void updatePlanetInput() {
        System.out.println("Enter the name of the planet you want to update:");
        String currentName = scanner.nextLine();

        String[] properties = {"new name", "new size", "new type"};
        String[] inputs = getUserInputs(scanner, properties);

        if (planetExist(currentName)) updatePlanet(currentName, inputs[0], Integer.parseInt(inputs[1]), inputs[2]);
        else System.out.println("Planet " + currentName + " does not exist.");
    }

    public static void updatePlanet(String currentName, String newName, int newSize, String newType) {
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
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
        em.close();
    }

    public static String[] getUserInputs(Scanner scanner, String[] properties) {
        String[] inputs = new String[properties.length];
        for (int i = 0; i < properties.length; i++) {
            System.out.println("Enter the " + properties[i] + ":");
            inputs[i] = scanner.nextLine();
        }
        return inputs;
    }

    public static boolean planetExist(String currentName) {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(p) FROM Planet p WHERE p.planetName = :planetName", Long.class);
        countQuery.setParameter("planetName", currentName);
        long count = countQuery.getSingleResult();
        em.close();
        return count > 0;
    }
}
