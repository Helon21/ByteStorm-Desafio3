package com.bytestorm.ms_propostas.web.dto.mapper;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import com.bytestorm.ms_propostas.web.dto.PropostaRespostaDTO;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor
public class PropostaMapper {

    public static Proposta paraProposta(PropostaCriarDTO dto) {
        return new ModelMapper().map(dto, Proposta.class);
    }

    public static PropostaRespostaDTO propostaParaDTO(Proposta proposta) {
        return new ModelMapper().map(proposta, PropostaRespostaDTO.class);
    }

}
