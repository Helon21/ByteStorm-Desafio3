package bytestorm.msresultados.web.dto.mapper;

import bytestorm.msresultados.entity.Resultado;
import bytestorm.msresultados.web.dto.ResultadoCriarDto;
import bytestorm.msresultados.web.dto.ResultadoRespostaDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultadoMapper {

    public static Resultado toResultado(ResultadoCriarDto dto) {
        return new Resultado(
                null,
                dto.getPropostaId(),
                dto.getFuncionarioId(),
                dto.getTitulo(),
                dto.getDescricao(),
                dto.getDataVotacao(),
                dto.getQtdAprovado(),
                dto.getQtdRejeitado(),
                Resultado.Status.valueOf(dto.getStatus())
        );
    }

    public static ResultadoRespostaDto toDto(Resultado resultado) {
        return new ModelMapper().map(resultado, ResultadoRespostaDto.class);
    }

}
