package com.bytestorm.ms_propostas.entity.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VotoId implements Serializable {

    @Column(name = "funcionario_id")
    private Long funcionarioId;

    @Column(name = "proposta_id")
    private Long propostaId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VotoId votoId = (VotoId) o;
        return Objects.equals(funcionarioId, votoId.funcionarioId) && Objects.equals(propostaId, votoId.propostaId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(funcionarioId);
        result = 31 * result + Objects.hashCode(propostaId);
        return result;
    }
}
