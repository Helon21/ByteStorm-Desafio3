package bytestorm.msresultados.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoCriarDto {

    private Long propostaId;
    private Long funcionarioId;
    private String titulo;
    private String descricao;
    private LocalDateTime dataVotacao;
    private Integer qtdAprovado;
    private Integer qtdRejeitado;
    private String status;

}
