package bytestorm.msresultados.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resultados")
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "proposta_id", nullable = false)
    private Long propostaId;
    @Column(name = "funcionario_id", nullable = false)
    private Long funcionarioId;
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;
    @Column(name = "descricao", nullable = false, length = 500)
    private String descricao;
    @Column(name = "data_votacao", nullable = false)
    private LocalDateTime dataVotacao;
    @Column(name = "qtd_aprovado", nullable = false)
    private Integer qtdAprovado;
    @Column(name = "qtd_rejeitado",nullable = false)
    private Integer qtdRejeitado;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        APROVADO, REJEITADO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resultado that = (Resultado) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
