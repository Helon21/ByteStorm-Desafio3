package com.bytestorm.ms_propostas.web.controller;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.service.PropostaService;
import com.bytestorm.ms_propostas.specification.PropostaSpecification;
import com.bytestorm.ms_propostas.web.dto.IniciarVotacaoDTO;
import com.bytestorm.ms_propostas.web.dto.PageableDto;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import com.bytestorm.ms_propostas.web.dto.PropostaRespostaDTO;
import com.bytestorm.ms_propostas.web.dto.mapper.PageableMapper;
import com.bytestorm.ms_propostas.web.dto.mapper.PropostaMapper;
import com.bytestorm.ms_propostas.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Slf4j
@Tag(name = "Propostas", description = "Contém todas as operações relativas a entidade proposta")
@RestController
@RequestMapping("/api/v1/propostas")
public class PropostaController {

    public final PropostaService propostaService;

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @Operation(summary = "Recuperar lista de propostas",
            parameters = {
                    @Parameter(in = QUERY, name = "titulo", description = "Filtra os resultados com base no título fornecido.",
                            content = @Content(schema = @Schema(type = "string"))
                    ),
                    @Parameter(in = QUERY, name = "funcionarioId", description = "Busca as propostas feita pelo id do funcionario.",
                            content = @Content(schema = @Schema(type = "integer"))
                    ),
                    @Parameter(in = QUERY, name = "status", description = "Filtra os resultados com base no status fornecido: <br> 'ATIVO', 'INATIVO', 'EM_VOTACAO', 'VOTACAO_ENCERRADA'.",
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
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Parametros inválido(s) ou mal formatado(s).",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping()
    public ResponseEntity<PageableDto> buscarResultados(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "funcionarioId", required = false) Long funcionarioId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "8") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        var spec = new PropostaSpecification(titulo, funcionarioId, status);

        Page<Proposta> resultado = propostaService.buscarPropostas(spec, pageable);
        return ResponseEntity.ok(PageableMapper.toDto(resultado));
    }

    @Operation(summary = "Cadastro de proposta", description = "Recurso para cadastrar proposta",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Proposta criada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropostaRespostaDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Id funcionario não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Funcionario inativado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos invalidos ou mal formatados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    public ResponseEntity<PropostaRespostaDTO> criarProposta(@RequestBody @Valid PropostaCriarDTO dto) {
        Proposta proposta = propostaService.criarProposta(dto);
        log.info("Proposta com id {} criada com sucesso.", proposta.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(PropostaMapper.propostaParaDTO(proposta));
    }


    @Operation(summary = "Inabilitar proposta", description = "Recurso para inabilitar proposta",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Proposta inabilitada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropostaRespostaDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Proposta não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PatchMapping("inabilitar-proposta/{id}")
    public ResponseEntity<PropostaRespostaDTO> inativarProposta(@PathVariable Long id) {
        Proposta proposta = propostaService.inativarProposta(id);
        log.info("Proposta com id {} inativada com sucesso.", id);
        return ResponseEntity.ok(PropostaMapper.propostaParaDTO(proposta));
    }

    @Operation(summary = "Iniciar votação", description = "Recurso para iniciar o processo de votação de uma proposta",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Proposta iniciada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropostaRespostaDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Proposta não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Proposta não pode entrar em votação",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos invalidos ou mal formatados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PatchMapping("iniciar-votacao/{id}")
    public ResponseEntity<PropostaRespostaDTO> iniciarVotacao(@PathVariable Long id, @RequestBody @Valid IniciarVotacaoDTO dto) {
        Proposta proposta = propostaService.iniciarVotacao(id, dto);
        return ResponseEntity.ok(PropostaMapper.propostaParaDTO(proposta));
    }
}