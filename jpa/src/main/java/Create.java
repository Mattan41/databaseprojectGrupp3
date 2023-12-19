import com.example.entities.Moon;
import com.example.entities.Planet;
import com.example.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
import java.util.Scanner;

public class Create {
    static EntityManager em = JPAUtil.getEntityManager();

    public static void createPlanet() {

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
            return;
        }

        if (planetSize <= 0 ) {
            System.out.println("Invalid input.");
            return;
        }
        if (planetType == null || planetType.isEmpty()) {
            System.out.println("Invalid input.");
            return;
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

    }

    public static void insertMoon() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter moon to add: ");
        String moonName = scanner.nextLine();

        if (moonName == null || moonName.isEmpty()) {
            System.out.println("Invalid input.");
            return;
        }

        Main.inTransaction(entityManager -> {
            TypedQuery<Moon> query = entityManager.createNamedQuery("Moon.findByName", Moon.class);
            query.setParameter("moonName", moonName);
            List<Moon> moons = query.getResultList();
            if (!moons.isEmpty()) {
                System.out.println( moonName + " already in database");
            }
        });


        System.out.print("Enter moonSize: ");
        Double moonSize = Double.valueOf(scanner.nextLine());

        if (moonSize <= 0 ) {
            System.out.println("Invalid input.");
            return;
        }


        System.out.print("Enter planetId: ");
        Integer planetId = Integer.parseInt(scanner.nextLine());

        Moon moon = new Moon();
        moon.setName(moonName);
        moon.setSize(moonSize);
        moon.setPlanetId(planetId);

        var transaction = em.getTransaction();
        transaction.begin();
        em.persist(moon);
        transaction.commit();
        em.close();
    }




    public static void insertStudent() {
    }



}


