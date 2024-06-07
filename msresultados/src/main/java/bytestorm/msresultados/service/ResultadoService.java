package bytestorm.msresultados.service;

import bytestorm.msresultados.entity.Resultado;
import bytestorm.msresultados.exception.ResultadoNaoEncontradoException;
import bytestorm.msresultados.repository.ResultadoRepository;
import bytestorm.msresultados.specification.ResultadoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;

    public Resultado buscarResultadoPorId(Long id) {
        return resultadoRepository.findById(id).orElseThrow(
                () -> new ResultadoNaoEncontradoException("Resultado com o id '" + id + "' não encontrado")
        );
    }

    public Page<Resultado> buscarResultados(ResultadoSpecification spec, Pageable pageable) {
        return resultadoRepository.findAll(spec, pageable);
    }
}
