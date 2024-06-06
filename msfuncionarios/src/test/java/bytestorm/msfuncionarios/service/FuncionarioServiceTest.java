package bytestorm.msfuncionarios.service;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.exceptions.FuncionarioNaoEncontradoException;
import bytestorm.msfuncionarios.repository.FuncionarioRepository;
import bytestorm.msfuncionarios.repository.projection.FuncionarioProjection;
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
    public void criarAluno_ComDadosValidos_RetornarFunciona() {
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
        Page<FuncionarioProjection> page = new PageImpl<>(Collections.singletonList(FUNCIONARIO_PROJECTION_PADRAO));
        Pageable pageable = PageRequest.of(0, 10);

        when(funcionarioRepository.findAllPageable(any(Pageable.class))).thenReturn(page);

        Page<FuncionarioProjection> result = funcionarioService.getAll(pageable);

        assertEquals(FUNCIONARIO_PROJECTION_PADRAO.getId(), result.getTotalElements());
        assertEquals(FUNCIONARIO_PROJECTION_PADRAO.getNome(), result.getContent().get(0).getNome());
        assertEquals(FUNCIONARIO_PROJECTION_PADRAO.getCpf(), result.getContent().get(0).getCpf());
    }
}
