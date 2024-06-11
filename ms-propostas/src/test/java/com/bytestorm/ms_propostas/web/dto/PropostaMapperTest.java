package com.bytestorm.ms_propostas.web.dto;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.web.dto.mapper.PropostaMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropostaMapperTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void paraProposta_DeveConverterCorretamente() {
        PropostaCriarDTO dto = new PropostaCriarDTO("Teste", "Descrição teste", 1L);

        Proposta proposta = PropostaMapper.paraProposta(dto);

        assertEquals(dto.getTitulo(), proposta.getTitulo());
        assertEquals(dto.getDescricao(), proposta.getDescricao());
        assertEquals(dto.getFuncionarioId(), proposta.getFuncionarioId());
    }

    @Test
    public void propostaParaDTO_DeveConverterCorretamente() {
        Proposta proposta = new Proposta(1L, 1L, "Teste", "Descrição teste");

        PropostaRespostaDTO dto = PropostaMapper.propostaParaDTO(proposta);

        assertEquals(proposta.getId(), dto.getId());
        assertEquals(proposta.getTitulo(), dto.getTitulo());
        assertEquals(proposta.getDescricao(), dto.getDescricao());
        assertEquals(proposta.getFuncionarioId(), dto.getFuncionarioId());
        assertEquals(proposta.getStatus(), dto.getStatus());
        assertEquals(proposta.getDataVotacao(), dto.getDataVotacao());
        assertEquals(proposta.getTempoVotacaoMinutos(), dto.getTempoVotacaoMinutos());
    }
}