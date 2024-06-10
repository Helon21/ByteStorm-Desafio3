package com.bytestorm.ms_propostas.web.controller;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.exception.PropostaNaoPodeSerInativadaException;
import com.bytestorm.ms_propostas.exception.PropostaNaoEncontradaException;
import com.bytestorm.ms_propostas.service.PropostaService;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import com.bytestorm.ms_propostas.web.dto.mapper.PropostaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.bytestorm.ms_propostas.common.PropostaConstantes.*;
import static com.bytestorm.ms_propostas.entity.Proposta.Status.ATIVO;
import static com.bytestorm.ms_propostas.entity.Proposta.Status.INATIVO;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class PropostaControllerTestes {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PropostaService propostaService;

//    @AfterEach
//    public void afterEach() {
//        PROPOSTA1.setStatus();
//    }

    @Test
    public void buscarTodasAsPropostas_RetornaPropostasComStatus200() throws Exception {
        when(propostaService.buscarTodasPropostas()).thenReturn(List.of(PROPOSTA1, PROPOSTA2));

        mockMvc.perform(
                get("/api/v1/propostas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].titulo", is("Propor melhoria na saúde")))
                .andExpect(jsonPath("$[0].descricao", is("Devemos focar na saúde pois é uma área que necessita de maiores cuidados")))
                .andExpect(jsonPath("$[0].ativo", is(true)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].titulo", is("Propor melhoria na educação")))
                .andExpect(jsonPath("$[1].descricao", is("Devemos focar na educação pois os jovens são o futuro da humanidade, e precisamos passar o conhecimento adiante para que não seja perdido")))
                .andExpect(jsonPath("$[1].status", is(ATIVO)));
    }

    @Test
    public void criarProposta_ComDadosValidos_ReturnPropostaComStatus201() throws Exception {
        Proposta proposta = PropostaMapper.paraProposta(DTO_CRIAR_PROPOSTA);
        when(propostaService.criarProposta(any(PropostaCriarDTO.class))).thenReturn(proposta);

        mockMvc.perform(
                        post("/api/v1/propostas")
                                .content(objectMapper.writeValueAsString(proposta)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is(proposta.getTitulo())))
                .andExpect(jsonPath("$.descricao", is(proposta.getDescricao())))
                .andExpect(jsonPath("$.status", is(proposta.getStatus())));
    }

    @Test
    public void criarProposta_ComDadosInvalidos_ReturnExceptionComStatus422() throws Exception {
        Proposta proposta = PropostaMapper.paraProposta(DTO_INVALIDO);

        mockMvc.perform(
                post("/api/v1/propostas")
                        .content(objectMapper.writeValueAsString(proposta)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void inativarProposta_ComIdValido_RetornaPropostaInativaComStatus200() throws Exception {
        Proposta proposta = PropostaMapper.paraProposta(DTO_CRIAR_PROPOSTA);
        when(propostaService.inativarProposta(ID_VALIDO)).thenReturn(proposta);

        mockMvc.perform(
                patch("/api/v1/propostas/inabilitar-proposta/1")
                        .content(objectMapper.writeValueAsString(proposta)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void inativarProposta_ComIdInvalido_RetornaStatus404() throws Exception {
        when(propostaService.inativarProposta(ID_INVALIDO)).thenThrow(new PropostaNaoEncontradaException("Proposta não encontrada ou inexistente, verifique se o id digitado está correto"));

        mockMvc.perform(
                patch("/api/v1/propostas/inabilitar-proposta/777"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void inativarProposta_ComIdValidoEPropostaInativa_RetornaStatus304() throws Exception {
        Proposta proposta = PropostaMapper.paraProposta(DTO_CRIAR_PROPOSTA);
        PROPOSTA1.setStatus(INATIVO);
        when(propostaService.inativarProposta(ID_VALIDO)).thenThrow(new PropostaNaoPodeSerInativadaException("Esta proposta já está inativa"));

        mockMvc.perform(
                patch("/api/v1/propostas/inabilitar-proposta/1")
                        .content(objectMapper.writeValueAsString(proposta)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotModified());
    }
}