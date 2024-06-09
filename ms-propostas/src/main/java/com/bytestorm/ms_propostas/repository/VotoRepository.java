package com.bytestorm.ms_propostas.repository;

import com.bytestorm.ms_propostas.entity.Voto;
import com.bytestorm.ms_propostas.entity.pk.VotoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, VotoId> {
}
