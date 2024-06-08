package bytestorm.msresultados.common;

import bytestorm.msresultados.entity.Resultado;

import java.time.LocalDateTime;

public class ResultadoConstantes {

    public static final Resultado PROPOSTA_1 = new Resultado(
            1L, "Cinema na Comunidade", "Projeto para construir um cinema comunitário para entretenimento local.",
            1L, LocalDateTime.of(2023, 5, 10, 14, 30), 50, 10, Resultado.Status.APROVADO);

    public static final Resultado PROPOSTA_2 = new Resultado(
            2L, "Piscina Pública", "Construção de uma nova piscina pública para lazer e atividades físicas.",
            2L, LocalDateTime.of(2023, 6, 20, 16, 0), 45, 15, Resultado.Status.REJEITADO);

    public static final Resultado PROPOSTA_3 = new Resultado(
            3L, "Parque Verde", "Criação de um parque verde para promover atividades ao ar livre e conservação ambiental.",
            3L, LocalDateTime.of(2023, 7, 15, 10, 45), 60, 5, Resultado.Status.APROVADO);

    public static final Resultado PROPOSTA_4 = new Resultado(
            4L, "Biblioteca Digital", "Desenvolvimento de uma biblioteca digital acessível para todos os cidadãos.",
            4L, LocalDateTime.of(2023, 8, 25, 13, 0), 55, 12, Resultado.Status.REJEITADO);

    public static final Resultado PROPOSTA_5 = new Resultado(
            5L, "Centro Esportivo", "Construção de um centro esportivo multifuncional para diversas modalidades.",
            1L, LocalDateTime.of(2023, 9, 10, 11, 30), 65, 8, Resultado.Status.APROVADO);

    public static final Resultado PROPOSTA_6 = new Resultado(
            6L, "Feira Cultural", "Organização de uma feira cultural anual para promover artes locais.",
            2L, LocalDateTime.of(2023, 10, 5, 15, 15), 70, 4, Resultado.Status.APROVADO);

}
