package com.bytestorm.ms_propostas.web.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class IniciarVotacaoDTO {

    @Min(1)
    private Integer tempoVotacaoMinutos;
}
