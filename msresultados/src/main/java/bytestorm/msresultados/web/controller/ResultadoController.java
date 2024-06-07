package bytestorm.msresultados.web.controller;

import bytestorm.msresultados.entity.Resultado;
import bytestorm.msresultados.service.ResultadoService;
import bytestorm.msresultados.specification.ResultadoSpecification;
import bytestorm.msresultados.web.dto.PageableDto;
import bytestorm.msresultados.web.dto.mapper.PageableMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/resultados")
public class ResultadoController {

    private final ResultadoService resultadoService;

    @GetMapping("/id/{id}")
    public ResponseEntity<Resultado> buscarPorId(@PathVariable Long id) {
        Resultado resultado = resultadoService.buscarResultadoPorId(id);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping(value = "/buscarResultados")
    public ResponseEntity<PageableDto> buscarResultados(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "dataVotacao", required = false) LocalDateTime dataVotacao,
            @RequestParam(value = "idFuncionario", required = false) Long idFuncionario,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "8") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        var spec = new ResultadoSpecification(titulo, dataVotacao, idFuncionario, status);

        Page<Resultado> resultado = resultadoService.buscarResultados(spec, pageable);
        return ResponseEntity.ok(PageableMapper.toDto(resultado));
    }
}