package bytestorm.msfuncionarios.web.controller;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.service.FuncionarioService;
import bytestorm.msfuncionarios.web.dto.FuncionarioRespostaDto;
import bytestorm.msfuncionarios.web.dto.FuncionarioCriarDto;
import bytestorm.msfuncionarios.web.dto.mapper.FuncionarioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<FuncionarioRespostaDto> criar(@RequestBody @Valid FuncionarioCriarDto FuncionarioCriardto) {
        Funcionario funcionario = funcionarioService.salvar(FuncionarioCriardto);
        return ResponseEntity.status(201).body(FuncionarioMapper.paraDto(funcionario));
    }

}
