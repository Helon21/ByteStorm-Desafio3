package com.bytestorm.ms_propostas.service;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.exception.PropostaNaoEncontradaException;
import com.bytestorm.ms_propostas.repository.PropostaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static com.bytestorm.ms_propostas.common.PropostaConstantes.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PropostaServiceTest {

    @Mock
    private PropostaRepository propostaRepository;

    @InjectMocks
    private PropostaService propostaService;

    @Test
    public void buscarTodasAsPropostas_RetornaTodasAsPropostas() {
        List<Proposta> listaPropostas = Arrays.asList(PROPOSTA1, PROPOSTA2);

        when(propostaRepository.findAll()).thenReturn(listaPropostas);

        List<Proposta> sut = propostaService.buscarTodasPropostas();

        assertThat(sut).isEqualTo(listaPropostas);

    }

    @Test
    public void CriarProposta_ComDadosValidos_RetornaProposta() {
        when(propostaRepository.save(PROPOSTA1)).thenReturn(PROPOSTA1);

        Proposta sut = propostaService.criarProposta(PROPOSTA1);

        assertThat(sut).isEqualTo(PROPOSTA1);
    }

    @Test
    public void CriarProposta_ComDadosInvalidos_RetornaException() {
        when(propostaRepository.save(PROPOSTA_INVALIDA)).thenThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY));

        assertThatThrownBy(() -> propostaService.criarProposta(PROPOSTA_INVALIDA)).isInstanceOf(HttpClientErrorException.class);

    };

    @Test
    public void InativarProposta_ComIdValido_RetornaPropostaInativa() {
    public void InativarProposta_RetornaPropostaInativa() {
        when(propostaRepository.findById(ID_VALIDO)).thenReturn(Optional.of(PROPOSTA_ATIVA));
        when(propostaRepository.save(PROPOSTA_INATIVA)).thenReturn(PROPOSTA_INATIVA);

        Proposta sut = propostaService.inativarProposta(ID_VALIDO);

        assertThat(sut.getAtivo()).isFalse();
    }
    @Test
    public void InativarProposta_ComIdInvalido_RetornarException() {
        when(propostaRepository.findById(ID_INVALIDO)).thenThrow(new PropostaNaoEncontradaException("Proposta não encontrada ou inexistente, verifique se o id digitado está correto"));

        assertThatThrownBy(() -> propostaService.inativarProposta(ID_INVALIDO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Proposta não encontrada ou inexistente, verifique se o id digitado está correto");
    }

    @Test
    public void InativarProposta_ComIdValidoEPropostaInativa_RetornaException() {
        PROPOSTA1.setAtivo(false);
        when(propostaRepository.findById(ID_VALIDO)).thenReturn(Optional.of(PROPOSTA1));

        assertThatThrownBy(() -> propostaService.inativarProposta(ID_VALIDO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Esta proposta já está inativa");
    }
}