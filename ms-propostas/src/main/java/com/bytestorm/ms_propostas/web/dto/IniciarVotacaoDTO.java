package com.bytestorm.ms_propostas.web.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IniciarVotacaoDTO implements Serializable {

    @Min(1)
    private Integer tempoVotacaoMinutos;
}
