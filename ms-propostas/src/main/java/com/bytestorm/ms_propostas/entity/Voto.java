package com.bytestorm.ms_propostas.entity;

import com.bytestorm.ms_propostas.entity.pk.VotoId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "votos")
public class Voto implements Serializable {

    @EmbeddedId
    private VotoId id;

    @ManyToOne
    @MapsId("propostaId")
    @JoinColumn(name = "proposta_id")
    private Proposta proposta;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        APROVADO, REJEITADO
    }

}
