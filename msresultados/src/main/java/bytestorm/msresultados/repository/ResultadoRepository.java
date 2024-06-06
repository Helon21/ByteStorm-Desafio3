package bytestorm.msresultados.repository;

import bytestorm.msresultados.entity.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

}
