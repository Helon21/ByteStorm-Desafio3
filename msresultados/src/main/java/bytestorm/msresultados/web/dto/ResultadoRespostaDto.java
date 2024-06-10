package bytestorm.msresultados.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoRespostaDto {

    private Long id;
    private Long propostaId;
    private Long funcionarioId;
    private String titulo;
    private String descricao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dataVotacao;
    private Integer qtdAprovado;
    private Integer qtdRejeitado;
    private String status;

}
