package com.bytestorm.ms_propostas.common;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import com.bytestorm.ms_propostas.web.dto.PropostaRespostaDTO;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class PropostaConstantes {

    public final static Proposta PROPOSTA1 = new Proposta(1L, 1L, "Propor melhoria na saúde", "Devemos focar na saúde pois é uma área que necessita de maiores cuidados");

    public final static PropostaCriarDTO DTO_CRIAR_PROPOSTA1 = new PropostaCriarDTO(PROPOSTA1.getTitulo(), PROPOSTA1.getDescricao(), PROPOSTA1.getFuncionarioId());

    public final static PropostaCriarDTO DTO_INVALIDO = new PropostaCriarDTO("", "", null);

}
