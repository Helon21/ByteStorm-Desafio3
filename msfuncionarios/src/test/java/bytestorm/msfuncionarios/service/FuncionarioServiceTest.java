package bytestorm.msfuncionarios.service;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.exceptions.CpfRepetidoException;
import bytestorm.msfuncionarios.exceptions.FuncionarioNaoEncontradoException;
import bytestorm.msfuncionarios.repository.FuncionarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Optional;

import static bytestorm.msfuncionarios.commom.FuncionariosConstantes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FuncionarioServiceTest {

    @InjectMocks
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Test
    public void criarFuncionario_ComDadosValidos_RetornarFuncionario() {
        when(funcionarioRepository.findByCpf(FUNCIONARIO_PADRAO_CRIAR_DTO.getCpf())).thenReturn(Optional.empty());
        when(funcionarioRepository.save(FUNCIONARIO_PADRAO)).thenReturn(FUNCIONARIO_PADRAO);

        Funcionario sut = funcionarioService.salvar(FUNCIONARIO_PADRAO_CRIAR_DTO);

        assertThat(sut).isEqualTo(FUNCIONARIO_PADRAO);
    }

    @Test
    public void criarFuncionario_ComDadosInvalidos_RetornarException() {
        when(funcionarioRepository.findByCpf(FUNCIONARIO_PADRAO_CRIAR_DTO.getCpf())).thenReturn(Optional.empty());
        when(funcionarioRepository.save(FUNCIONARIO_INVALIDO)).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> funcionarioService.salvar(FUNCIONARIO_INVALIDO_CRIAR_DTO)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void criarFuncionario_ComCpfRepetido_RetornarException() {
        when(funcionarioRepository.findByCpf(FUNCIONARIO_PADRAO_CRIAR_DTO.getCpf())).thenReturn(Optional.of(FUNCIONARIO_PADRAO));

        assertThatThrownBy(() -> funcionarioService.salvar(FUNCIONARIO_INVALIDO_CRIAR_DTO)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void alterarFuncionario_ComDadosValidosEMesmoCpf_RetornarFuncionario200() {
        when(funcionarioRepository.findById(PEDRO.getId())).thenReturn(Optional.of(PEDRO));
        when(funcionarioRepository.findByCpf(PEDRO.getCpf())).thenReturn(Optional.of(PEDRO));
        when(funcionarioRepository.save(PEDRO)).thenReturn(PEDRO);

        Funcionario sut = funcionarioService.alterar(PEDRO.getId(), PEDRO_ALTERAR_DTO);

        assertThat(sut).isEqualTo(PEDRO);
    }

    @Test
    public void alterarFuncionario_ComDadosValidosENovoCpf_RetornarFuncionario200() {
        when(funcionarioRepository.findById(PEDRO.getId())).thenReturn(Optional.of(PEDRO));
        when(funcionarioRepository.findByCpf(PEDRO.getCpf())).thenReturn(Optional.empty());
        when(funcionarioRepository.save(PEDRO)).thenReturn(PEDRO);

        Funcionario sut = funcionarioService.alterar(PEDRO.getId(), PEDRO_ALTERAR_DTO);

        assertThat(sut).isEqualTo(PEDRO);
    }

    @Test
    public void alterarFuncionario_ComIdInexistente_DeveLancarFuncionarioNaoEncontradoException() {
        when(funcionarioRepository.findById(PEDRO.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> funcionarioService.alterar(PEDRO.getId(), PEDRO_ALTERAR_DTO)).isInstanceOf(FuncionarioNaoEncontradoException.class);
    }

    @Test
    public void alterarFuncionario_ComCpfJaCadastrado_DeveLancarCpfRepetidoException() {
        Funcionario outroFuncionario = new Funcionario(
                5L, "Outro Funcionario",
                PEDRO_ALTERAR_DTO.getCpf(),
                LocalDate.of(1990, 1, 1),
                Funcionario.Status.ATIVO,
                Funcionario.Sexo.MASCULINO
        );

        when(funcionarioRepository.findById(PEDRO.getId())).thenReturn(Optional.of(PEDRO));
        when(funcionarioRepository.findByCpf(PEDRO_ALTERAR_DTO.getCpf())).thenReturn(Optional.of(outroFuncionario));

        assertThatThrownBy(() -> funcionarioService.alterar(PEDRO.getId(), PEDRO_ALTERAR_DTO)).isInstanceOf(CpfRepetidoException.class);
    }
}
