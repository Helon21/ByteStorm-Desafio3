package bytestorm.msresultados.service;

import bytestorm.msresultados.entity.Resultado;
import bytestorm.msresultados.exception.ResultadoNaoEncontradoException;
import bytestorm.msresultados.repository.ResultadoRepository;
import bytestorm.msresultados.specification.ResultadoSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.Optional;

import static bytestorm.msresultados.common.ResultadoConstantes.PROPOSTA_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResultadoServiceTest {

    @InjectMocks
    private ResultadoService resultadoService;

    @Mock
    private ResultadoRepository resultadoRepository;

    @Test
    public void buscarResultadoPorPropostaId_ComIdExistente_RetornarResultado() {
        when(resultadoRepository.findByPropostaId(PROPOSTA_1.getId())).thenReturn(Optional.of(PROPOSTA_1));

        Resultado sut = resultadoService.buscarResultadoPorPropostaId(PROPOSTA_1.getId());

        assertThat(sut).isEqualTo(PROPOSTA_1);
    }

    @Test
    public void buscarResultadoPorPropostaId_ComIdInexistente_RetornarException() {
        when(resultadoRepository.findByPropostaId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resultadoService.buscarResultadoPorPropostaId(1L)).isInstanceOf(ResultadoNaoEncontradoException.class);
    }

    @Test
    public void buscarResultados_RetornarPaginaDeResultados() {
        Pageable pageable = PageRequest.of(0, 1);
        ResultadoSpecification spec = new ResultadoSpecification(null, null, null, null);
        Page<Resultado> expectedPage = new PageImpl<>(Collections.singletonList(PROPOSTA_1), pageable, 1);

        when(resultadoRepository.findAll(any(ResultadoSpecification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Resultado> actualPage = resultadoService.buscarResultados(spec, pageable);

        assertThat(actualPage).isEqualTo(expectedPage);
    }
}
