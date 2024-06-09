package com.bytestorm.ms_propostas.web.controller;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.service.PropostaService;
import com.bytestorm.ms_propostas.web.dto.IniciarVotacaoDTO;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import com.bytestorm.ms_propostas.web.dto.PropostaRespostaDTO;
import com.bytestorm.ms_propostas.web.dto.mapper.PropostaMapper;
import com.bytestorm.ms_propostas.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Propostas", description = "Contém todas as operações relativas a entidade proposta")
@RestController
@RequestMapping("/api/v1/propostas")
public class PropostaController {

    public final PropostaService propostaService;

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @Operation(summary = "Buscar todas as propostas", description = "Recurso para realizar consulta de todas as propostas cadastradas",
        responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropostaRespostaDTO.class))),
        }
    )
    @GetMapping
    public ResponseEntity<List<PropostaRespostaDTO>> buscarTodasPropostas() {
        List<Proposta> proposta = propostaService.buscarTodasPropostas();
        List<PropostaRespostaDTO> propostaDTO = proposta.stream().map(PropostaMapper::propostaParaDTO).toList();
        return ResponseEntity.ok(propostaDTO);
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