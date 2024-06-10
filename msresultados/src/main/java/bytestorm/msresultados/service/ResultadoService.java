package bytestorm.msresultados.service;

import bytestorm.msresultados.entity.Resultado;
import bytestorm.msresultados.exception.ResultadoNaoEncontradoException;
import bytestorm.msresultados.repository.ResultadoRepository;
import bytestorm.msresultados.specification.ResultadoSpecification;
import bytestorm.msresultados.web.dto.ResultadoCriarDto;
import bytestorm.msresultados.web.dto.mapper.ResultadoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Log4j2
@RequiredArgsConstructor
@Service
public class ResultadoService {

    private final ResultadoRepository resultadoRepository;

    @Transactional(readOnly = true)
    public Resultado buscarResultadoPorId(Long id) {
        return resultadoRepository.findById(id).orElseThrow(
                () -> new ResultadoNaoEncontradoException("Resultado com o id '" + id + "' não encontrado")
        );
    }

    @Transactional(readOnly = true)
    public Page<Resultado> buscarResultados(ResultadoSpecification spec, Pageable pageable) {
        return resultadoRepository.findAll(spec, pageable);
    }

    @Transactional
    public void cadastrarResultado(ResultadoCriarDto dto) {
        Resultado resultado = ResultadoMapper.toResultado(dto);
        resultadoRepository.save(resultado);
        log.info("Resultado da proposta com id {} persistido no banco dados", resultado.getPropostaId());
    }
}
