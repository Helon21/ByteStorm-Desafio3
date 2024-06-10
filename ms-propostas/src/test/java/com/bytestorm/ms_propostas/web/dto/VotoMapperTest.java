package com.bytestorm.ms_propostas.web.dto;

import org.junit.jupiter.api.Test;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.entity.Voto;
import com.bytestorm.ms_propostas.web.dto.mapper.VotoMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VotoMapperTest {

    @Test
    public void paraVoto_DeveConverterCorretamente() {
        Long funcionarioId = 1L;
        Proposta proposta = new Proposta(1L, 1L, "Teste", "Descrição teste");
        Voto.Status status = Voto.Status.APROVADO;

        Voto voto = VotoMapper.paraVoto(funcionarioId, proposta, status);

        assertEquals(funcionarioId, voto.getId().getFuncionarioId());
        assertEquals(proposta.getId(), voto.getId().getPropostaId());
        assertEquals(proposta, voto.getProposta());
        assertEquals(status, voto.getStatus());
    }
}