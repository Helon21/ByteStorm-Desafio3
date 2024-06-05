package bytestorm.msfuncionarios.repository;

import bytestorm.msfuncionarios.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

}