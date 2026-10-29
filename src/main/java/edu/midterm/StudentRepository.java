package edu.midterm;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StudentRepository extends JpaRepository<Student,Long> {
 java.util.List<Student> findByNameContainingIgnoreCase(String name);
}
