package bytestorm.msfuncionarios.repository;

import bytestorm.msfuncionarios.entity.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByCpf(String cpf);

    @Query("select f from Funcionario f")
    Page<Funcionario> findAllPageable(Pageable pageable);
}