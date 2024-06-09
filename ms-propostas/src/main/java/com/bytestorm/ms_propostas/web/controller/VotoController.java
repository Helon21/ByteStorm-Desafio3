package com.bytestorm.ms_propostas.web.controller;

import com.bytestorm.ms_propostas.service.VotoService;
import com.bytestorm.ms_propostas.web.dto.PropostaRespostaDTO;
import com.bytestorm.ms_propostas.web.dto.VotoCriarDto;
import com.bytestorm.ms_propostas.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Votos", description = "Contém todas as operações relativas a entidade votos")
@RestController
@RequestMapping("/api/v1/votos")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @Operation(summary = "Iniciar votação", description = "Recurso para iniciar o processo de votação de uma proposta",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Proposta iniciada com sucesso",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Funcionário ou proposta não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Funcionário está inativo ou proposta não está aberta a votação",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos invalidos ou mal formatados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping("votar/{id}")
    public ResponseEntity<Void> votarEmProposta(@PathVariable Long id, @RequestBody @Valid VotoCriarDto dto) {
        votoService.votarEmProposta(id, dto);
        return ResponseEntity.noContent().build();
    }
}
