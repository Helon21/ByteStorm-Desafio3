package com.bytestorm.ms_propostas.web.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class IniciarVotacaoDTO {

    @Min(1)
    private Integer tempoVotacaoMinutos;
}
