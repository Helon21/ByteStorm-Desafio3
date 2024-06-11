package com.bytestorm.ms_propostas.specification;

import com.bytestorm.ms_propostas.entity.Proposta;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


public class PropostaSpecification implements Specification<Proposta> {

    private String titulo;
    private Long funcionarioId;
    private String status;

    public PropostaSpecification(String titulo, Long funcionarioId, String status) {
        this.titulo = titulo;
        this.funcionarioId = funcionarioId;
        this.status = status;
    }

    @Override
    public Predicate toPredicate(Root<Proposta> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (titulo != null && !titulo.isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"));
        }

        if (funcionarioId != null) {
            predicates.add(criteriaBuilder.equal(root.get("funcionarioId"), funcionarioId));
        }

        if (status != null) {
            Proposta.Status statusEnum = null;
            if (status.equalsIgnoreCase("INATIVO")) {
                statusEnum = Proposta.Status.INATIVO;
            } else if (status.equalsIgnoreCase("EM_VOTACAO")) {
                statusEnum = Proposta.Status.EM_VOTACAO;
            } else if (status.equalsIgnoreCase("VOTACAO_ENCERRADA")) {
                statusEnum = Proposta.Status.VOTACAO_ENCERRADA;
            } else {
                statusEnum = Proposta.Status.ATIVO;
            }
            predicates.add(criteriaBuilder.equal(root.get("status"), statusEnum));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}