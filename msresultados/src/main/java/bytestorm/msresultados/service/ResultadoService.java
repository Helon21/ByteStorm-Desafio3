package bytestorm.msresultados.service;

import bytestorm.msresultados.entity.Resultado;
import bytestorm.msresultados.exception.ResultadoNaoEncontradoException;
import bytestorm.msresultados.repository.ResultadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;

    public Resultado buscarResultadoPorId(Long id) {
        return resultadoRepository.findById(id).orElseThrow(
                () -> new ResultadoNaoEncontradoException("Resultado com o id '" + id + "' não encontrado")
        );
    }
}
