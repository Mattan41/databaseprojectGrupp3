import com.example.entities.Moon;
import com.example.entities.Planet;
import com.example.entities.Student;
import jakarta.persistence.TypedQuery;

import java.util.Scanner;

    public class Delete {
        public static void deletePlanet() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the name of the planet you want to delete: ");
            String planetName = scanner.nextLine();

            Main.inTransaction(entityManager -> {
                TypedQuery<Planet> query = entityManager.createNamedQuery("Planet.findByName", Planet.class);
                query.setParameter("planetName", planetName);

                Planet planet = query.getSingleResult();
                if (planet != null) {
                    entityManager.remove(planet);
                    System.out.println("The planet is deleted!");
                } else {
                    System.out.println("Planet not found with name: " + planetName);
                }
            });
        }

        public static void deleteMoon() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the name of the moon you want to delete: ");
            String moonName = scanner.nextLine();

            Main.inTransaction(entityManager -> {
                TypedQuery<Moon> query = entityManager.createNamedQuery("Moon.findByName", Moon.class);
                query.setParameter("moonName", moonName);

                Moon moon = query.getSingleResult();
                if (moon != null) {
                    entityManager.remove(moon);
                    System.out.println("The moon is deleted!");
                } else {
                    System.out.println("Moon not found with name: " + moonName);
                }
            });
        }
    }
