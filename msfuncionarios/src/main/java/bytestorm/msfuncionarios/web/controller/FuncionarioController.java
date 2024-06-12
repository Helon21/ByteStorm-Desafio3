package bytestorm.msfuncionarios.web.controller;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.service.FuncionarioService;
import bytestorm.msfuncionarios.web.dto.FuncionarioAlterarStatusDto;
import bytestorm.msfuncionarios.web.dto.FuncionarioRespostaDto;
import bytestorm.msfuncionarios.web.dto.FuncionarioCriarDto;
import bytestorm.msfuncionarios.web.dto.PageableDto;
import bytestorm.msfuncionarios.web.dto.mapper.FuncionarioMapper;
import bytestorm.msfuncionarios.web.dto.mapper.PageableMapper;
import bytestorm.msfuncionarios.web.exception.MensagemErro;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

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
                    @ApiResponse(responseCode = "409", description = "CPF informado já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválido(s) ou mal formatado(s)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class)))
            })
    @PostMapping
    public ResponseEntity<FuncionarioRespostaDto> criar(@RequestBody @Valid FuncionarioCriarDto FuncionarioCriardto) {
        Funcionario funcionario = funcionarioService.salvar(FuncionarioCriardto);
        return ResponseEntity.status(201).body(FuncionarioMapper.paraDto(funcionario));
    }

    @Operation(summary = "Altera os dados de um Funcionário",
            description = "Recurso o atualizar os dados funcionário no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso alterado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FuncionarioRespostaDto.class))),
                    @ApiResponse(responseCode = "409", description = "CPF informado já cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválido(s) ou mal formatado(s)",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class)))
            })
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioRespostaDto> alterar(@PathVariable Long id, @RequestBody @Valid FuncionarioCriarDto FuncionarioCriardto) {
        Funcionario funcionario = funcionarioService.alterar(id, FuncionarioCriardto);
        return ResponseEntity.ok().body(FuncionarioMapper.paraDto(funcionario));
    }

    @Operation(summary = "Localizar um funcionário por id", description = "Recurso para localizar um funcionário pelo ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = FuncionarioRespostaDto.class))),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
            })
    @GetMapping("/id/{id}")
    public ResponseEntity<FuncionarioRespostaDto> buscarPorId(@PathVariable Long id) {
        Funcionario funcionario = funcionarioService.buscarFuncionarioPorId(id);
        return ResponseEntity.ok(FuncionarioMapper.paraDto(funcionario));
    }

    @Operation(summary = "Localizar um funcionário por cpf", description = "Recurso para localizar um funcionário pelo CPF.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = FuncionarioRespostaDto.class))),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
            })
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<FuncionarioRespostaDto> buscarPorCpf(@PathVariable String cpf) {
        Funcionario funcionario = funcionarioService.buscarFuncionarioPorCpf(cpf);
        return ResponseEntity.ok(FuncionarioMapper.paraDto(funcionario));
    }

    @Operation(summary = "Recuperar lista de funcionários",
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a página retornada"
                    ),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Representa o total de elementos por página"
                    ),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "name,asc")),
                            description = "Representa a ordenação dos resultados. Aceita multiplos critérios de ordenação.")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = PageableDto.class))
                    )
            })
    @GetMapping
    public ResponseEntity<PageableDto> buscarTodos(@Parameter(hidden = true) @PageableDefault(size=5, sort={"nome"}, page=0) Pageable pageable) {
        Page<Funcionario> funcionarios = funcionarioService.getAll(pageable);
        Page<FuncionarioRespostaDto> funcionariosDto = funcionarios.map(FuncionarioMapper::paraDto);
        return ResponseEntity.ok(PageableMapper.toDto(funcionariosDto));
    }

    @Operation(summary = "Altera o status Funcionários",
            description = "Recurso para alterar o status de um funcionário no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FuncionarioRespostaDto.class))),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = MensagemErro.class))),
                    @ApiResponse(responseCode = "422", description = "Campos inválidos ou mal formatados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class)))
            })
    @PatchMapping("/{id}")
    public ResponseEntity<FuncionarioRespostaDto> alterarStatus(@PathVariable Long id, @RequestBody @Valid FuncionarioAlterarStatusDto statusDto) {
        Funcionario funcionario = funcionarioService.alterarStatus(id, statusDto);
        return ResponseEntity.ok().body(FuncionarioMapper.paraDto(funcionario));
    }

}
