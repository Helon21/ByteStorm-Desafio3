package com.bytestorm.ms_propostas.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotoCriarDto {

    @NotNull
    @Min(1)
    private Long funcionarioId;

    @NotBlank
    @Pattern(regexp = "APROVADO|REJEITADO")
    private String status;

}
