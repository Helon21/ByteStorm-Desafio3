package com.bytestorm.ms_propostas.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PropostaCriarDTO {

    @NotBlank
    @Size(min = 1, max = 100)
    private String titulo;

    @NotBlank
    @Size(min = 1, max = 500)
    private String descricao;

    @NotNull
    @Min(1)
    private Long funcionarioId;
}
