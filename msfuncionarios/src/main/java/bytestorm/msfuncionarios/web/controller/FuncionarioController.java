package bytestorm.msfuncionarios.web.controller;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.service.FuncionarioService;
import bytestorm.msfuncionarios.web.dto.FuncionarioRespostaDto;
import bytestorm.msfuncionarios.web.dto.FuncionarioCriarDto;
import bytestorm.msfuncionarios.web.dto.mapper.FuncionarioMapper;
import bytestorm.msfuncionarios.web.exception.MensagemErro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Funcionarios", description = "Contém todas as operações relativas a entidade funcionário")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @Operation(summary = "Realizar Cadastro de Funcionários",
            description = "Recurso o cadastro de um funcionário no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FuncionarioRespostaDto.class))),
                    @ApiResponse(responseCode = "409", description = "CPF do funcionário já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos ou mal formatados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class)))
            })
    @PostMapping
    public ResponseEntity<FuncionarioRespostaDto> criar(@RequestBody @Valid FuncionarioCriarDto FuncionarioCriardto) {
        Funcionario funcionario = funcionarioService.salvar(FuncionarioCriardto);
        return ResponseEntity.status(201).body(FuncionarioMapper.paraDto(funcionario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioRespostaDto> buscarPorId(@PathVariable Long id) {
        Funcionario funcionario = funcionarioService.buscarFuncionarioPorId(id);
        return ResponseEntity.ok(FuncionarioMapper.paraDto(funcionario));
    }

}
