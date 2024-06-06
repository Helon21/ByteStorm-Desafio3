package com.bytestorm.ms_propostas.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PropostaCriarDTO {

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

}
