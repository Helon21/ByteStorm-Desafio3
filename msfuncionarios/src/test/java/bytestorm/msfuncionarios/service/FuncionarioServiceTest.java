package bytestorm.msfuncionarios.service;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.exceptions.FuncionarioNaoEncontradoException;
import bytestorm.msfuncionarios.repository.FuncionarioRepository;
import bytestorm.msfuncionarios.web.dto.FuncionarioAlterarStatusDto;
import bytestorm.msfuncionarios.exceptions.CpfRepetidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static bytestorm.msfuncionarios.commom.FuncionariosConstantes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    public void alterarStatus_ComFuncionarioExistente_DeveAlterarStatus() {
        FuncionarioAlterarStatusDto statusDto = new FuncionarioAlterarStatusDto();
        statusDto.setStatus("INATIVO");

        when(funcionarioRepository.findById(JORGE.getId())).thenReturn(Optional.of(JORGE));
        when(funcionarioRepository.save(JORGE)).thenReturn(JORGE);

        Funcionario result = funcionarioService.alterarStatus(JORGE.getId(), statusDto);

        assertThat(result.getStatus()).isEqualTo(Funcionario.Status.INATIVO);
    }

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

    @Test
    public void buscarUsuarioPorId_ComIdExistente_RetornarFuncionario() {
        when(funcionarioRepository.findById(JORGE.getId())).thenReturn(Optional.of(JORGE));

        Funcionario sut = funcionarioService.buscarFuncionarioPorId(JORGE.getId());

        assertThat(sut).isEqualTo(JORGE);
    }

    @Test
    public void buscarUsuarioPorId_ComIdInexistente_RetornarException() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> funcionarioService.buscarFuncionarioPorId(1L)).isInstanceOf(FuncionarioNaoEncontradoException.class);
    }

    @Test
    public void buscarUsuarioPorCpf_ComCpfExistente_RetornarFuncionario() {
        when(funcionarioRepository.findByCpf(JORGE.getCpf())).thenReturn(Optional.of(JORGE));

        Funcionario sut = funcionarioService.buscarFuncionarioPorCpf(JORGE.getCpf());

        assertThat(sut).isEqualTo(JORGE);
    }

    @Test
    public void buscarUsuarioPorCpf_ComCpfInexistente_RetornarException() {
        when(funcionarioRepository.findByCpf("11111111111")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> funcionarioService.buscarFuncionarioPorCpf("11111111111")).isInstanceOf(FuncionarioNaoEncontradoException.class);
    }

    @Test
    void testGetAll() {
        Page<Funcionario> page = new PageImpl<>(Collections.singletonList(JOAO));
        Pageable pageable = PageRequest.of(0, 10);

        when(funcionarioRepository.findAllPageable(any(Pageable.class))).thenReturn(page);

        Page<Funcionario> result = funcionarioService.getAll(pageable);

        assertEquals(result.getTotalElements(), 1);
        assertEquals(JOAO.getId(), result.getContent().get(0).getId());
        assertEquals(JOAO.getNome(), result.getContent().get(0).getNome());
        assertEquals(JOAO.getCpf(), result.getContent().get(0).getCpf());

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
