import com.example.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Scanner;

public class Read {
    static void readStudent(EntityManager em) {
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
}
