import com.example.entities.Moon;
import com.example.entities.SolarSystem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class Read {
    static EntityManager em = JPAUtil.getEntityManager();

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
}
