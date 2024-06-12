package bytestorm.msfuncionarios.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FuncionarioAlterarStatusDto {
    @NotBlank
    @Pattern(regexp = "ATIVO|INATIVO")
    private String status;
}
