package com.bytestorm.ms_propostas.web.dto.mapper;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.entity.Voto;
import com.bytestorm.ms_propostas.entity.pk.VotoId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VotoMapper {

    public static Voto paraVoto(Long funcionarioId, Proposta proposta, Voto.Status status) {
        VotoId votoId = new VotoId(funcionarioId, proposta.getId());
        return new Voto(votoId, proposta, status);
    }

}
