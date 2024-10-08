package bytestorm.msresultados.specification;

import bytestorm.msresultados.entity.Resultado;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ResultadoSpecification implements Specification<Resultado> {

    private String titulo;
    private LocalDateTime dataVotacao;
    private Long funcionarioId;
    private String status;

    public ResultadoSpecification(String titulo, LocalDateTime dataVotacao, Long funcionarioId, String status) {
        this.titulo = titulo;
        this.dataVotacao = dataVotacao;
        this.funcionarioId = funcionarioId;
        this.status = status;
    }

    @Override
    public Predicate toPredicate(Root<Resultado> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        
        if (titulo != null && !titulo.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"));
        }
        
        if (dataVotacao != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataVotacao"), dataVotacao));
        }
        
        if (funcionarioId != null) {
            predicates.add(criteriaBuilder.equal(root.get("funcionarioId"), funcionarioId));
        }
        
        if (status != null) {
            Resultado.Status statusEnum = status.toUpperCase().equals("REJEITADO") ? Resultado.Status.REJEITADO : Resultado.Status.APROVADO;
            predicates.add(criteriaBuilder.equal(root.get("status"), statusEnum));
        }
        
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}