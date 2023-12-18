import com.example.entities.Planet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.Scanner;

public class Create {
    static EntityManager em = JPAUtil.getEntityManager();

    public static Runnable createPlanet() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter planet to add: ");
        String planetName = scanner.nextLine();
        System.out.print("Enter planetSize: ");
        Integer planetSize = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter planet type: ");
        String planetType = scanner.nextLine();
        System.out.print("Enter solarSystemId: ");
        Integer solarSystemId = Integer.parseInt(scanner.nextLine());

        // Validate user input
        if (planetName == null || planetName.isEmpty()) {
            System.out.println("Invalid input.");
            return null;
        }

        if (planetSize <= 0 ) {
            System.out.println("Invalid input.");
            return null;
        }
        if (planetType == null || planetType.isEmpty()) {
            System.out.println("Invalid input.");
            return null;
        }


        Planet planet = new Planet();
        planet.setPlanetName(planetName);
        planet.setPlanetSize(planetSize);
        planet.setPlanetType(planetType);
        planet.setSolarSystemId(solarSystemId);

        var transaction = em.getTransaction();
        transaction.begin();
        em.persist(planet);
        transaction.commit();
        em.close();
        return null;
    }
}
