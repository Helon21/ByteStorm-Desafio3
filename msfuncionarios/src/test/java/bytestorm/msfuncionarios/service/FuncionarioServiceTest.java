package bytestorm.msfuncionarios.service;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.exceptions.FuncionarioNaoEncontradoException;
import bytestorm.msfuncionarios.repository.FuncionarioRepository;
import bytestorm.msfuncionarios.web.dto.FuncionarioAlterarStatusDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static bytestorm.msfuncionarios.commom.FuncionariosConstantes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FuncionarioServiceTest {

    @InjectMocks
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Test
    public void criarAluno_ComDadosValidos_RetornarAluno() {
        when(funcionarioRepository.findByCpf(FUNCIONARIO_PADRAO_CRIAR_DTO.getCpf())).thenReturn(Optional.empty());
        when(funcionarioRepository.save(FUNCIONARIO_PADRAO)).thenReturn(FUNCIONARIO_PADRAO);

        Funcionario sut = funcionarioService.salvar(FUNCIONARIO_PADRAO_CRIAR_DTO);

        assertThat(sut).isEqualTo(FUNCIONARIO_PADRAO);
    }

    @Test
    public void criarAluno_ComDadosInvalidos_RetornarException() {
        when(funcionarioRepository.findByCpf(FUNCIONARIO_PADRAO_CRIAR_DTO.getCpf())).thenReturn(Optional.empty());
        when(funcionarioRepository.save(FUNCIONARIO_INVALIDO)).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> funcionarioService.salvar(FUNCIONARIO_INVALIDO_CRIAR_DTO)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void criarAluno_ComCpfRepetido_RetornarException() {
        when(funcionarioRepository.findByCpf(FUNCIONARIO_PADRAO_CRIAR_DTO.getCpf())).thenReturn(Optional.of(FUNCIONARIO_PADRAO));

        assertThatThrownBy(() -> funcionarioService.salvar(FUNCIONARIO_INVALIDO_CRIAR_DTO)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void alterarStatus_ComFuncionarioExistente_DeveAlterarStatus() {
        FuncionarioAlterarStatusDto statusDto = new FuncionarioAlterarStatusDto();
        statusDto.setStatus("INATIVO");

        when(funcionarioRepository.findById(JORGE.getId())).thenReturn(Optional.of(JORGE));
        when(funcionarioRepository.save(JORGE)).thenReturn(JORGE);

        Funcionario result = funcionarioService.alterarStatus(JORGE.getId(), statusDto);

        assertThat(result.getStatus()).isEqualTo(Funcionario.Status.INATIVO);
    }

    @Test
    public void alterarStatus_ComFuncionarioNaoExistente_DeveLancarExcecao() {
        Long id = 1L;
        FuncionarioAlterarStatusDto statusDto = new FuncionarioAlterarStatusDto();
        statusDto.setStatus("INATIVO");

        when(funcionarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> funcionarioService.alterarStatus(id, statusDto))
                .isInstanceOf(FuncionarioNaoEncontradoException.class);
    }

}
