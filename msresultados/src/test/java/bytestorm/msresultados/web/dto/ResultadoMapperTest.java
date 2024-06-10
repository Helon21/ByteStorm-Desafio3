package bytestorm.msresultados.web.dto;

import bytestorm.msresultados.entity.Resultado;
import bytestorm.msresultados.web.dto.mapper.ResultadoMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultadoMapperTest {

    @Test
    public void testToResultado() {
        ResultadoCriarDto dto = new ResultadoCriarDto(
                1L, 2L, "Test Title", "Test Description",
                LocalDateTime.of(2023, 6, 10, 10, 0),
                100, 20, "APROVADO"
        );

        Resultado resultado = ResultadoMapper.toResultado(dto);

        assertEquals(dto.getPropostaId(), resultado.getPropostaId());
        assertEquals(dto.getFuncionarioId(), resultado.getFuncionarioId());
        assertEquals(dto.getTitulo(), resultado.getTitulo());
        assertEquals(dto.getDescricao(), resultado.getDescricao());
        assertEquals(dto.getDataVotacao(), resultado.getDataVotacao());
        assertEquals(dto.getQtdAprovado(), resultado.getQtdAprovado());
        assertEquals(dto.getQtdRejeitado(), resultado.getQtdRejeitado());
        assertEquals(Resultado.Status.APROVADO, resultado.getStatus());
    }

    @Test
    public void testToDto() {
        Resultado resultado = new Resultado(
                1L, 1L, 2L, "Test Title", "Test Description",
                LocalDateTime.of(2023, 6, 10, 10, 0),
                100, 20, Resultado.Status.APROVADO
        );

        ResultadoRespostaDto dto = ResultadoMapper.toDto(resultado);

        assertEquals(resultado.getId(), dto.getId());
        assertEquals(resultado.getPropostaId(), dto.getPropostaId());
        assertEquals(resultado.getFuncionarioId(), dto.getFuncionarioId());
        assertEquals(resultado.getTitulo(), dto.getTitulo());
        assertEquals(resultado.getDescricao(), dto.getDescricao());
        assertEquals(resultado.getDataVotacao(), dto.getDataVotacao());
        assertEquals(resultado.getQtdAprovado(), dto.getQtdAprovado());
        assertEquals(resultado.getQtdRejeitado(), dto.getQtdRejeitado());
        assertEquals(resultado.getStatus().name(), dto.getStatus());
    }
}