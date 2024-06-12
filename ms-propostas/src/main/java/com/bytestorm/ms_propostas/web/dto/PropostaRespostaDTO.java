package com.bytestorm.ms_propostas.web.dto;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dataVotacao;
    private Integer tempoVotacaoMinutos;

}
