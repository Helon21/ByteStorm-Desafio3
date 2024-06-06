package bytestorm.msresultados.service;

import bytestorm.msresultados.repository.ResultadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;

}
