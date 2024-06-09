package com.bytestorm.ms_propostas.web.controller;

import com.bytestorm.ms_propostas.service.VotoService;
import com.bytestorm.ms_propostas.web.dto.VotoCriarDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/votos")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping("votar/{id}")
    public ResponseEntity<Void> votarEmProposta(@PathVariable Long id, @RequestBody @Valid VotoCriarDto dto) {
        votoService.votarEmProposta(id, dto);
        return ResponseEntity.noContent().build();
    }
}
