package bytestorm.msfuncionarios.web.dto;

import bytestorm.msfuncionarios.entity.Funcionario;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FuncionarioRespostaDto {

    private Long id;
    private String nome;
    private String cpf;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataNascimento;
    private Funcionario.Status status;
    private Funcionario.Sexo sexo;

}