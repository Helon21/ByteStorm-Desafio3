package com.bytestorm.ms_propostas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Proposta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="funcionario_id", nullable = false)
    private Long funcionarioId;

    @Column(name="titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name="descricao", nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private  Status status;

    @Column(name = "data_votacao", nullable = true)
    private LocalDateTime dataVotacao;

    @Column(name = "tempo_votacao_minutos", nullable = true)
    private Integer tempoVotacaoMinutos;

    public enum Status {
        ATIVO, INATIVO, EM_VOTACAO, VOTACAO_ENCERRADA
    }

    @JsonIgnore
    @OneToMany(mappedBy = "id.propostaId")
    private List<Voto> voto = new ArrayList<>();

    public Proposta(Long id, Long funcionarioId, String titulo, String descricao) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = Status.ATIVO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposta proposta = (Proposta) o;
        return Objects.equals(id, proposta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}