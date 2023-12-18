import com.example.entities.Planet;
import com.example.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Scanner;

public class Create {


    public static void createPlanet(EntityManager em) {

        System.out.print("Enter planet to add: ");
        Scanner scanner = new Scanner(System.in);
        String planetName = scanner.nextLine();

        System.out.print("Enter planetSize: ");
        Integer planetSize = scanner.nextInt();
        System.out.print("Enter planet type: ");
        String planetType = scanner.nextLine();

        // Validate user input
        if (planetName == null || planetName.isEmpty()) {
            System.out.println("Invalid input.");
            return;
        }
        Planet planet = new Planet();
        planet.setPlanetName(planetName);
        planet.setPlanetSize(planetSize);
        planet.setPlanetType(planetType);

        var transaction = em.getTransaction();
        transaction.begin();
        em.persist(planet);
        transaction.commit();
            em.close();
    }
}
