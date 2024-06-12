package com.bytestorm.ms_propostas.repository;

import com.bytestorm.ms_propostas.entity.Voto;
import com.bytestorm.ms_propostas.entity.pk.VotoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VotoRepository extends JpaRepository<Voto, VotoId> {

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.proposta.id = :propostaId AND v.status = com.bytestorm.ms_propostas.entity.Voto.Status.APROVADO")
    Integer countAprovadosByPropostaId(@Param("propostaId") Long propostaId);

    @Query("SELECT COUNT(v) FROM Voto v WHERE v.proposta.id = :propostaId AND v.status = com.bytestorm.ms_propostas.entity.Voto.Status.REJEITADO")
    Integer countRejeitadosByPropostaId(@Param("propostaId") Long propostaId);

}
