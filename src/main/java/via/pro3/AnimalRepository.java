package via.pro3;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByDate(LocalDate date);
    List<Animal> findByOrigin(String origin);
}
