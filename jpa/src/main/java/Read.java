import com.example.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class Read {
    static EntityManager em = JPAUtil.getEntityManager();

    //Visa alla planeter
    public static void showAllPlanets() {
        TypedQuery<Planet> query = em.createQuery("SELECT p FROM Planet p", Planet.class);
        List<Planet> planets = query.getResultList();
        planets.forEach(System.out::println);
        em.close();
    }
    public static void showAllMoons() {
        TypedQuery<Moon> query = em.createQuery("SELECT m FROM Moon m", Moon.class);
        List<Moon> moons = query.getResultList();
        moons.forEach(System.out::println);
        em.close();
    }

    public static void showAllSolarSystems() {
        TypedQuery<SolarSystem> query = em.createQuery("SELECT s FROM SolarSystem s", SolarSystem.class);
        List<SolarSystem> solarSystems = query.getResultList();
        solarSystems.forEach(System.out::println);
        em.close();
    }

    public static void showAllStudents() {
        TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
        List<Student> students = query.getResultList();
        students.forEach(System.out::println);
        em.close();
    }

    public static void showAllTests() {
        TypedQuery<Test> query = em.createQuery("SELECT t FROM Test t", Test.class);
        List<Test> tests = query.getResultList();
        tests.forEach(System.out::println);
        em.close();
    }


}
