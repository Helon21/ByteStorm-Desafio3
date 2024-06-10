package com.bytestorm.ms_propostas.service;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.exception.*;
import com.bytestorm.ms_propostas.repository.PropostaRepository;
import com.bytestorm.ms_propostas.repository.VotoRepository;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static com.bytestorm.ms_propostas.common.PropostaConstantes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropostaServiceTestes {

    @Mock
    private FuncionarioService funcionarioService;
    @Mock
    private PropostaRepository propostaRepository;
    @Mock
    private VotoRepository votoRepository;
    @Mock
    private KafkaTemplate<String, Serializable> kafkaTemplate;
    @Mock
    private ScheduledExecutorService scheduler;
    @Captor
    private ArgumentCaptor<Runnable> captor;


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
    public void buscarTodasPropostas_DeveRetornarListaDePropostas() {
        List<Proposta> propostasMock = new ArrayList<>();
        propostasMock.add(new Proposta());
        propostasMock.add(new Proposta());
        when(propostaRepository.findAll()).thenReturn(propostasMock);

        List<Proposta> propostas = propostaService.buscarTodasPropostas();

        assertEquals(propostasMock.size(), propostas.size());
        verify(propostaRepository, times(1)).findAll();
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
