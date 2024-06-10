package bytestorm.msresultados.web.controller;

import bytestorm.msresultados.entity.Resultado;
import bytestorm.msresultados.service.ResultadoService;
import bytestorm.msresultados.specification.ResultadoSpecification;
import bytestorm.msresultados.web.dto.PageableDto;
import bytestorm.msresultados.web.dto.ResultadoRespostaDto;
import bytestorm.msresultados.web.dto.mapper.PageableMapper;
import bytestorm.msresultados.web.dto.mapper.ResultadoMapper;
import bytestorm.msresultados.web.expection.MensagemErro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/resultados")
public class ResultadoController {

    private final ResultadoService resultadoService;

    @Operation(summary = "Localizar um resultado por id da proposta", description = "Recurso para localizar um resultado pelo id da proposta.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso.",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ResultadoRespostaDto.class))),
                    @ApiResponse(responseCode = "404", description = "resultado não encontrado.",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
            })
    @GetMapping("/id/{id}")
    public ResponseEntity<ResultadoRespostaDto> buscarPorPropostaId(@PathVariable Long id) {
        Resultado resultado = resultadoService.buscarResultadoPorPropostaId(id);
        return ResponseEntity.ok(ResultadoMapper.toDto(resultado));
    }

    @Operation(summary = "Recuperar lista de funcionários",
            parameters = {
                    @Parameter(in = QUERY, name = "titulo", description = "Filtra os resultados com base no título fornecido.",
                            content = @Content(schema = @Schema(type = "string"))
                    ),
                    @Parameter(in = QUERY, name = "dataVotacao", description = "Filtra os resultados a partir da data especificada.",
                            content = @Content(schema = @Schema(type = "string", format = "date-time"))
                    ),
                    @Parameter(in = QUERY, name = "idFuncionario", description = "Busca as propostas feita pelo id do funcionario.",
                            content = @Content(schema = @Schema(type = "integer"))
                    ),
                    @Parameter(in = QUERY, name = "status", description = "Filtra os resultados com base no status fornecido: <br> 'aprovado' ou 'rejeitado'.",
                            content = @Content(schema = @Schema(type = "string"))
                    ),
                    @Parameter(in = QUERY, name = "page", description = "Representa a página retornada.",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))
                    ),
                    @Parameter(in = QUERY, name = "size", description = "Representa o total de elementos por página.",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "8"))
                    ),
                    @Parameter(in = QUERY, name = "direction", description = "Representa a ordenação do resultado.",
                            content = @Content(schema = @Schema(type = "string", defaultValue = "asc")))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso.",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = PageableDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Parametros inválido(s) ou mal formatado(s).",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class)))
            })
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

        Page<Resultado> resultadoPage  = resultadoService.buscarResultados(spec, pageable);
        Page<ResultadoRespostaDto> dtoPage = resultadoPage.map(ResultadoMapper::toDto);

        return ResponseEntity.ok(PageableMapper.toDto(dtoPage));
    }
}