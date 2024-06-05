package bytestorm.msfuncionarios.web.dto;

import bytestorm.msfuncionarios.entity.Funcionario;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FuncionarioCriarDto {

        @NotNull
        @Size(min = 2, max = 100)
        private String nome;
        @Size(min = 11, max = 11)
        @NotNull
        @CPF
        private String cpf;
        @NotNull
        LocalDate dataNascimento;
        @NotNull
        @Pattern(regexp = "MASCULINO|FEMININO")
        private String sexo;

}