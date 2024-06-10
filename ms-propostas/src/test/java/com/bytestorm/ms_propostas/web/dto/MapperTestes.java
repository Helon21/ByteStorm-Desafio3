package com.bytestorm.ms_propostas.web.dto;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.web.dto.mapper.PropostaMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MapperTestes {

    @Test
    public void converterDTOParaProposta_RetornaPropostaDTO() {
        PropostaCriarDTO criarDto = new PropostaCriarDTO("algum titulo", "alguma descrição", 1L);
        Proposta proposta = PropostaMapper.paraProposta(criarDto);

        assertNotNull(proposta);
        assertEquals(criarDto.getTitulo(), proposta.getTitulo());
        assertEquals(criarDto.getDescricao(), proposta.getDescricao());
    }

    @Test
    public void converterPropostaParaDTO_RetornaPropostaRespostaDTO() {
        Proposta proposta = new Proposta(1L, 1L, "Alguma descrição", "Alguma descrição");
        PropostaRespostaDTO propostaDTO = PropostaMapper.propostaParaDTO(proposta);


        assertNotNull(propostaDTO);
        assertNotNull(propostaDTO.getId());
        assertEquals(proposta.getTitulo(), propostaDTO.getTitulo());
        assertEquals(proposta.getDescricao(), propostaDTO.getDescricao());
        assertNotNull(propostaDTO.getStatus());
    }
}
