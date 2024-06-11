package com.bytestorm.ms_propostas.repository;

import com.bytestorm.ms_propostas.entity.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PropostaRepository extends JpaRepository<Proposta, Long>, JpaSpecificationExecutor<Proposta> {

}
