package com.bytestorm.ms_propostas.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoDTO implements Serializable {
    private Long propostaId;
    private String titulo;
    private String descricao;
    private Long funcionarioId;
    private LocalDateTime dataVotacao;
    private Integer qtdAprovado;
    private Integer qtdRejeitado;
    private String status;
}