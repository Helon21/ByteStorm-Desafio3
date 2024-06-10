package bytestorm.msresultados.web.listener;

import bytestorm.msresultados.service.ResultadoService;
import bytestorm.msresultados.web.dto.ResultadoCriarDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class JsonListener {

    private final ResultadoService resultadoService;

    @SneakyThrows
    @KafkaListener(topics = "resultado-topico", groupId = "grupo1", containerFactory = "jsonContainerFactory")
    public void consumerKafka(@Payload ResultadoCriarDto resultadoDto) {
        log.info("Resultado recebido: {}", resultadoDto.toString());
        resultadoService.cadastrarResultado(resultadoDto);
    }

}
