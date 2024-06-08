package com.bytestorm.ms_propostas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="funcionarioId", nullable = false)
    private Long funcionarioId;

    @Column(name="titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name="descricao", nullable = false, length = 500)
    private String descricao;

    @Column(name="ativo")
    private Boolean ativo;

    @OneToMany(mappedBy = "id.propostaId")
    private List<Voto> voto = new ArrayList<>();

    public Proposta(Long id, Long funcionarioId, String titulo, String descricao, Boolean ativo) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.ativo = ativo;
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