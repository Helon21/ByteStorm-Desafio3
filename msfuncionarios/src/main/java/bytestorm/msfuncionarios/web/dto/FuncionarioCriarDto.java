package bytestorm.msfuncionarios.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FuncionarioCriarDto {
    @NotBlank
    @Size(min = 2, max = 100)
    private String nome;
    @Size(min = 11, max = 11)
    @NotBlank
    @CPF
    private String cpf;
    @NotNull
    private LocalDate dataNascimento;
    @NotBlank
    @Pattern(regexp = "MASCULINO|FEMININO")
    private String sexo;
}