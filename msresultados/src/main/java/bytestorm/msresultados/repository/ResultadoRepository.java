package bytestorm.msresultados.repository;

import bytestorm.msresultados.entity.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ResultadoRepository extends JpaRepository<Resultado, Long>, JpaSpecificationExecutor<Resultado> {

    Optional<Resultado> findByPropostaId(Long propostaId);

}
