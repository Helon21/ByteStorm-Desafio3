package com.bytestorm.ms_propostas.service;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.exception.*;
import com.bytestorm.ms_propostas.repository.PropostaRepository;
import com.bytestorm.ms_propostas.specification.PropostaSpecification;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static com.bytestorm.ms_propostas.common.PropostaConstantes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropostaServiceTestes {

    @Mock
    private FuncionarioService funcionarioService;
    @Mock
    private PropostaRepository propostaRepository;

    @InjectMocks
    private PropostaService propostaService;

    @Test
    public void buscarPropostaPorId_QuandoPropostaExiste_RetornaProposta() {
        when(propostaRepository.findById(PROPOSTA1.getId())).thenReturn(Optional.of(PROPOSTA1));

        Proposta propostaEncontrada = propostaService.buscarPropostaPorId(PROPOSTA1.getId());

        assertEquals(PROPOSTA1, propostaEncontrada);
        verify(propostaRepository, times(1)).findById(PROPOSTA1.getId());
    }

    @Test
    public void buscarPropostaPorId_QuandoPropostaNaoExiste_LancaExcecao() {
        Long id = 1L;
        when(propostaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PropostaNaoEncontradaException.class, () -> propostaService.buscarPropostaPorId(id));
        verify(propostaRepository, times(1)).findById(id);
    }

    @Test
    public void testBuscarPropostas() {
        PropostaSpecification spec = mock(PropostaSpecification.class);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Proposta> expectedPage = new PageImpl<>(Collections.emptyList());
        when(propostaRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Proposta> resultPage = propostaService.buscarPropostas(spec, pageable);

        assertEquals(expectedPage, resultPage);
        verify(propostaRepository, times(1)).findAll(spec, pageable);
    }

    @Test
    public void criarProposta_ComFuncionarioAtivo_RetornarProposta() {
        // Arrange
        PropostaCriarDTO dto = new PropostaCriarDTO("Teste", "Descrição teste", 1L);
        Proposta proposta = new Proposta(1L, dto.getFuncionarioId(), dto.getTitulo(), dto.getDescricao());
        when(funcionarioService.funcionarioEhAtivo(any())).thenReturn(true);
        when(propostaRepository.save(any())).thenReturn(proposta);

        // Act
        Proposta propostaCriada = propostaService.criarProposta(dto);

        // Assert
        assertThat(propostaCriada).isNotNull();
        assertThat(propostaCriada.getFuncionarioId()).isEqualTo(dto.getFuncionarioId());
        assertThat(propostaCriada.getTitulo()).isEqualTo(dto.getTitulo());
        assertThat(propostaCriada.getDescricao()).isEqualTo(dto.getDescricao());
        assertThat(propostaCriada.getStatus()).isEqualTo(Proposta.Status.ATIVO);
        assertThat(propostaCriada.getDataVotacao()).isNull();
        assertThat(propostaCriada.getTempoVotacaoMinutos()).isNull();
        verify(propostaRepository, times(1)).save(any());
    }

    @Test
    public void criarProposta_ComFuncionarioInativo_ThrowsException() {
        PropostaCriarDTO propostaDto = new PropostaCriarDTO();
        propostaDto.setFuncionarioId(1L);

        when(funcionarioService.funcionarioEhAtivo(1L)).thenReturn(false);


        assertThrows(FuncionarioInativoException.class, () -> propostaService.criarProposta(propostaDto));
        verify(propostaRepository, never()).save(any());
    }

    @Test
    public void inativarProposta_QuandoPropostaAtiva_InativaProposta() {
        when(propostaRepository.findById(PROPOSTA1.getId())).thenReturn(Optional.of(PROPOSTA1));
        when(propostaRepository.save(PROPOSTA1)).thenReturn(PROPOSTA1);

        Proposta propostaInativada = propostaService.inativarProposta(PROPOSTA1.getId());

        assertEquals(Proposta.Status.INATIVO, propostaInativada.getStatus());
        verify(propostaRepository, times(1)).save(PROPOSTA1);
    }

    @Test
    public void inativarProposta_QuandoPropostaEmVotacao_LancaExcecao() {
        Proposta proposta = new Proposta();
        proposta.setId(1L);
        proposta.setStatus(Proposta.Status.EM_VOTACAO);

        when(propostaRepository.findById(proposta.getId())).thenReturn(Optional.of(proposta));

        assertThrows(PropostaNaoPodeSerInativadaException.class, () -> propostaService.inativarProposta(proposta.getId()));
        verify(propostaRepository, never()).save(proposta);
    }

    @Test
    public void inativarProposta_QuandoPropostaEmVotacaoEncerrada_LancaExcecao() {
        Proposta proposta = new Proposta();
        proposta.setId(1L);
        proposta.setStatus(Proposta.Status.VOTACAO_ENCERRADA);

        when(propostaRepository.findById(proposta.getId())).thenReturn(Optional.of(proposta));

        assertThrows(PropostaNaoPodeSerInativadaException.class, () -> propostaService.inativarProposta(proposta.getId()));
        verify(propostaRepository, never()).save(proposta);
    }
}
