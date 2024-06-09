package com.bytestorm.ms_propostas.web.dto;

import com.bytestorm.ms_propostas.entity.Proposta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropostaRespostaDTO {

    private Long id;
    private Long funcionarioId;
    private String titulo;
    private String descricao;
    private Proposta.Status status;
    private LocalDateTime dataVotacao;
    private Integer tempoVotacaoMinutos;

}
