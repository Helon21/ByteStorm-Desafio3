package com.bytestorm.ms_propostas.common;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import com.bytestorm.ms_propostas.web.dto.PropostaRespostaDTO;

import java.time.LocalDateTime;
import java.util.Date;

public class PropostaConstantes {

    public final static Proposta PROPOSTA1 = new Proposta(1L, 1L, "Propor melhoria na saúde", "Devemos focar na saúde pois é uma área que necessita de maiores cuidados");

    public final static Proposta PROPOSTA2 = new Proposta(2L, 1L, "Propor melhoria na educação", "Devemos focar na educação pois os jovens são o futuro da humanidade, e precisamos passar o conhecimento adiante para que não seja perdido");

    public final static Proposta PROPOSTA_INVALIDA = new Proposta(3L, null, null, null);

    public final static Proposta PROPOSTA_ATIVA = new Proposta(1L, 1L, "Titulo da proposta", "Descrição da proposta");

    public final static Proposta PROPOSTA_INATIVA = new Proposta(1L, 1L, "Titulo da proposta", "Descrição da proposta");

    public final static PropostaCriarDTO DTO_CRIAR_PROPOSTA = new PropostaCriarDTO("Propor melhoria na saúde", "Deve se focar na saúde pois é uma área que necessita de maiores cuidados", 1L);

    public final static PropostaCriarDTO DTO_INVALIDO = new PropostaCriarDTO("", "", null);

    public final static PropostaRespostaDTO DTO_RESPOSTA_PROPOSTA = new PropostaRespostaDTO(1L, 1L, "Propor melhoria na saúde", "Deve se focar na saúde pois é uma área que necessita de maiores cuidados", Proposta.Status.ATIVO, LocalDateTime.now(), 1);

    public final static Long ID_VALIDO = 1L;

    public final static Long ID_INVALIDO = 777L;
}
