package com.bytestorm.ms_propostas.web.controller;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.service.PropostaService;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import com.bytestorm.ms_propostas.web.dto.PropostaRespostaDTO;
import com.bytestorm.ms_propostas.web.dto.mapper.PropostaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            }
    )
    @PostMapping
    public ResponseEntity<PropostaRespostaDTO> criarProposta(@RequestBody @Valid PropostaCriarDTO dto) {
        Proposta proposta = PropostaMapper.paraProposta(dto);
        propostaService.criarProposta(proposta);
        PropostaRespostaDTO propostaCriada = PropostaMapper.propostaParaDTO(proposta);
        return ResponseEntity.status(HttpStatus.CREATED).body(propostaCriada);
    }


    @Operation(summary = "Inabilitar proposta", description = "Recurso para inabilitar proposta",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Proposta inabilitada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropostaRespostaDTO.class))),
            }
    )
    @PatchMapping("inabilitar-proposta/{id}")
    public ResponseEntity<PropostaRespostaDTO> inativarProposta(@PathVariable Long id) {
        Proposta proposta = propostaService.inativarProposta(id);
        return ResponseEntity.ok(PropostaMapper.propostaParaDTO(proposta));
    }
}