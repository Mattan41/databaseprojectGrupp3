import com.example.entities.Planet;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class Update {
    EntityManager em = JPAUtil.getEntityManager();
    public void updatePlanetName(String currentName, String newName, int newSize, String newType) {
        em.getTransaction().begin();
        TypedQuery<Planet> query = em.createQuery("SELECT p FROM Planet p WHERE p.planetName = :planetName", Planet.class);
        query.setParameter("planetName", currentName);
        try {
            Planet planet = query.getSingleResult();
            planet.setPlanetName(newName);
            planet.setPlanetSize(newSize);
            planet.setPlanetType(newType);
            em.merge(planet);
            System.out.println("Planet " + currentName + " updated");
        } catch (NoResultException e) {
            System.out.println("Planet " + currentName + " not found");
        }
        em.getTransaction().commit();
        em.close();
    }
}
