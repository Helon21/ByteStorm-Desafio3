package com.bytestorm.ms_propostas.service;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.entity.Voto;
import com.bytestorm.ms_propostas.entity.pk.VotoId;
import com.bytestorm.ms_propostas.exception.FuncionarioInativoException;
import com.bytestorm.ms_propostas.exception.PropostaNaoEncontradaException;
import com.bytestorm.ms_propostas.exception.PropostaNaoEstaEmVotacaoException;
import com.bytestorm.ms_propostas.exception.VotoJaExisteException;
import com.bytestorm.ms_propostas.repository.VotoRepository;
import com.bytestorm.ms_propostas.web.dto.VotoCriarDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VotoServiceTestes {

    @Mock
    private FuncionarioService funcionarioService;
    @Mock
    private PropostaService propostaService;
    @Mock
    private VotoRepository votoRepository;

    @InjectMocks
    private VotoService votoService;

    @Test
    public void votarEmProposta_QuandoFuncionarioAtivoEPropostaEmVotacao_CriaEVotaNoVoto() {
        // Arrange
        Long propostaId = 1L;
        Long funcionarioId = 1L;
        Proposta proposta = new Proposta(propostaId, funcionarioId, "Título", "Descrição");
        proposta.setStatus(Proposta.Status.EM_VOTACAO);
        VotoCriarDto dto = new VotoCriarDto(funcionarioId, "APROVADO");
        Voto voto = new Voto(new VotoId(funcionarioId, propostaId), proposta, Voto.Status.APROVADO);

        when(funcionarioService.funcionarioEhAtivo(funcionarioId)).thenReturn(true);
        when(propostaService.buscarPropostaPorId(propostaId)).thenReturn(proposta);
        when(votoRepository.findById(any())).thenReturn(Optional.empty());
        when(votoRepository.save(any())).thenReturn(voto);

        Voto sut = votoService.votarEmProposta(propostaId, dto);

        assertNotNull(sut);
        assertEquals(funcionarioId, sut.getId().getFuncionarioId());
        assertEquals(propostaId, sut.getId().getPropostaId());
        assertEquals(Voto.Status.APROVADO, sut.getStatus());

        verify(votoRepository).save(any());
    }

    @Test
    public void votarEmProposta_QuandoPropostaComStatusAtivo_DeveLancarExcecao() {
        Long propostaId = 1L;
        Long funcionarioId = 1L;
        Proposta proposta = new Proposta(propostaId, funcionarioId, "Título", "Descrição");
        proposta.setStatus(Proposta.Status.ATIVO);
        VotoCriarDto dto = new VotoCriarDto(funcionarioId, "APROVADO");

        when(funcionarioService.funcionarioEhAtivo(funcionarioId)).thenReturn(true);
        when(propostaService.buscarPropostaPorId(propostaId)).thenReturn(proposta);

        assertThrows(PropostaNaoEstaEmVotacaoException.class, () -> {
            votoService.votarEmProposta(propostaId, dto);
        });

        verify(funcionarioService).funcionarioEhAtivo(funcionarioId);
        verify(propostaService).buscarPropostaPorId(propostaId);
    }

    @Test
    public void votarEmProposta_QuandoPropostaComStatusInativo_DeveLancarExcecao() {
        Long propostaId = 1L;
        Long funcionarioId = 1L;
        Proposta proposta = new Proposta(propostaId, funcionarioId, "Título", "Descrição");
        proposta.setStatus(Proposta.Status.INATIVO);
        VotoCriarDto dto = new VotoCriarDto(funcionarioId, "APROVADO");

        when(funcionarioService.funcionarioEhAtivo(funcionarioId)).thenReturn(true);
        when(propostaService.buscarPropostaPorId(propostaId)).thenReturn(proposta);

        assertThrows(PropostaNaoEstaEmVotacaoException.class, () -> {
            votoService.votarEmProposta(propostaId, dto);
        });

        verify(funcionarioService).funcionarioEhAtivo(funcionarioId);
        verify(propostaService).buscarPropostaPorId(propostaId);
    }

    @Test
    public void votarEmProposta_QuandoPropostaComStatusVotacaoEncerrada_DeveLancarExcecao() {
        Long propostaId = 1L;
        Long funcionarioId = 1L;
        Proposta proposta = new Proposta(propostaId, funcionarioId, "Título", "Descrição");
        proposta.setStatus(Proposta.Status.VOTACAO_ENCERRADA);
        VotoCriarDto dto = new VotoCriarDto(funcionarioId, "APROVADO");

        when(funcionarioService.funcionarioEhAtivo(funcionarioId)).thenReturn(true);
        when(propostaService.buscarPropostaPorId(propostaId)).thenReturn(proposta);

        assertThrows(PropostaNaoEstaEmVotacaoException.class, () -> {
            votoService.votarEmProposta(propostaId, dto);
        });

        verify(funcionarioService).funcionarioEhAtivo(funcionarioId);
        verify(propostaService).buscarPropostaPorId(propostaId);
    }

    @Test
    public void votarEmProposta_QuandoPropostaNaoEncontrada_DeveLancarExcecao() {
        Long propostaId = 1L;
        Long funcionarioId = 1L;
        VotoCriarDto dto = new VotoCriarDto(funcionarioId, "APROVADO");

        when(funcionarioService.funcionarioEhAtivo(funcionarioId)).thenReturn(true);
        when(propostaService.buscarPropostaPorId(propostaId)).thenThrow(
                new PropostaNaoEncontradaException("Proposta com o id '" + propostaId + "' não encontrada!")
        );

        assertThrows(PropostaNaoEncontradaException.class, () -> {
            votoService.votarEmProposta(propostaId, dto);
        });

        verify(funcionarioService).funcionarioEhAtivo(funcionarioId);
        verify(propostaService).buscarPropostaPorId(propostaId);
        verify(votoRepository, never()).save(any());
    }

    @Test
    public void votarEmProposta_QuandoFuncionarioInativo_DeveLancarExcecao() {
        Long propostaId = 1L;
        Long funcionarioId = 1L;
        Proposta proposta = new Proposta(propostaId, funcionarioId, "Título", "Descrição");
        proposta.setStatus(Proposta.Status.EM_VOTACAO);
        VotoCriarDto dto = new VotoCriarDto(funcionarioId, "APROVADO");

        when(funcionarioService.funcionarioEhAtivo(funcionarioId)).thenReturn(false);

        // Act & Assert
        assertThrows(FuncionarioInativoException.class, () -> {
            votoService.votarEmProposta(propostaId, dto);
        });

        verify(funcionarioService).funcionarioEhAtivo(funcionarioId);
        verify(propostaService, never()).buscarPropostaPorId(any());
    }

    @Test
    public void votarEmProposta_QuandoVotoJaExiste_DeveLancarExcecao() {
        Long propostaId = 1L;
        Long funcionarioId = 1L;
        Proposta proposta = new Proposta(propostaId, funcionarioId, "Título", "Descrição");
        proposta.setStatus(Proposta.Status.EM_VOTACAO);
        VotoCriarDto dto = new VotoCriarDto(funcionarioId, "APROVADO");
        Voto votoExistente = new Voto(new VotoId(funcionarioId, propostaId), proposta, Voto.Status.APROVADO);

        when(funcionarioService.funcionarioEhAtivo(funcionarioId)).thenReturn(true);
        when(propostaService.buscarPropostaPorId(propostaId)).thenReturn(proposta);
        when(votoRepository.findById(any())).thenReturn(Optional.of(votoExistente));

        // Act & Assert
        assertThrows(VotoJaExisteException.class, () -> {
            votoService.votarEmProposta(propostaId, dto);
        });

        verify(funcionarioService).funcionarioEhAtivo(funcionarioId);
        verify(propostaService).buscarPropostaPorId(propostaId);
        verify(votoRepository).findById(any());
        verify(votoRepository, never()).save(any());
    }
}
