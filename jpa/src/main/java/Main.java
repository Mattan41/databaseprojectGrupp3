import com.example.entities.Student;
import com.example.util.Menu;
import jakarta.persistence.*;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        mainMenu();
        EntityManager em = JPAUtil.getEntityManager();

        System.out.print("Enter search term: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        // Validate user input
        if (name == null || name.isEmpty()) {
            System.out.println("Invalid input.");
            return;
        }

        TypedQuery<Student> query = em.createQuery("SELECT c FROM Student c WHERE c.studentName = :name", Student.class);
        query.setParameter("name", name);
        List<Student> students = query.getResultList();
        students.forEach(System.out::println);

        em.close();
    }

    static void inTransaction(Consumer<EntityManager> work) {
        try (EntityManager entityManager = JPAUtil.getEntityManager()) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                work.accept(entityManager);
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        }
    }
    public static void mainMenu(){
        var mainMenu = new Menu("Main Menu!");
        mainMenu.addMenuItem("1 - Planet", Main::planetsMenu);
        mainMenu.addMenuItem("2 - Moon", Main::moonMenu);
        mainMenu.addMenuItem("3 - Solar system", Main::solarSystemMenu);
        mainMenu.addMenuItem("4 - Student", Main::studentMenu);
        mainMenu.addMenuItem("5 - Test", Main::testMenu);
        mainMenu.displayMenu();
    }

    public static void planetsMenu(){
        var planetMenu = new Menu("Planet Menu!");
        planetMenu.addMenuItem("1 - Show all planets", () -> System.out.println("show all!"));
        planetMenu.addMenuItem("2 - Insert a new planet", () -> System.out.println("insert!"));
        planetMenu.addMenuItem("3 - Update a planet", () -> System.out.println("update!"));
        planetMenu.addMenuItem("4 - Delete a planet", () -> System.out.println("delete"));
        planetMenu.displayMenu();
    }
    public static void moonMenu(){
        var moonMenu = new Menu("Moon Menu!");
        moonMenu.addMenuItem("1 - Show all moons", Read::showAllMoons);
        moonMenu.addMenuItem("2 - Insert a new moon", () -> System.out.println("insert!"));
        moonMenu.addMenuItem("3 - Update a moon", () -> System.out.println("update!"));
        moonMenu.addMenuItem("4 - Delete a moon", () -> System.out.println("delete"));
        moonMenu.displayMenu();
    }
    public static void solarSystemMenu(){
        var solarSystemMenu = new Menu("Solar system Menu!");
        solarSystemMenu.addMenuItem("1 - Show all solar systems", Read::showAllSolarSystems);
        solarSystemMenu.addMenuItem("2 - Insert a new solar system", () -> System.out.println("insert!"));
        solarSystemMenu.addMenuItem("3 - Update a solar system", () -> System.out.println("update!"));
        solarSystemMenu.addMenuItem("4 - Delete a solar system", () -> System.out.println("delete"));
        solarSystemMenu.displayMenu();
    }
    public static void studentMenu(){
        var studentMenu = new Menu("Student Menu!");
        studentMenu.addMenuItem("1 - Show all Students", () -> System.out.println("show all!"));
        studentMenu.addMenuItem("2 - Insert a new Student", () -> System.out.println("insert!"));
        studentMenu.addMenuItem("3 - Update a Student", () -> System.out.println("update!"));
        studentMenu.addMenuItem("4 - Delete a Student", () -> System.out.println("delete"));
        studentMenu.displayMenu();
    }

    public static void testMenu(){
        var testMenu = new Menu("test Menu!");
        testMenu.addMenuItem("1 - Show all tests", () -> System.out.println("show all!"));
        testMenu.addMenuItem("2 - Insert a new test", () -> System.out.println("insert!"));
        testMenu.addMenuItem("3 - Update a test", () -> System.out.println("update!"));
        testMenu.addMenuItem("4 - Delete a test", () -> System.out.println("delete"));
        testMenu.displayMenu();
    }

}
