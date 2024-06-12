package bytestorm.msfuncionarios.web.dto;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.web.dto.FuncionarioCriarDto;
import bytestorm.msfuncionarios.web.dto.FuncionarioRespostaDto;
import bytestorm.msfuncionarios.web.dto.mapper.FuncionarioMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FuncionarioMapperTest {

    @Test
    public void testParaFuncionario() {
        FuncionarioCriarDto criarDto = new FuncionarioCriarDto("João", "12345678901", LocalDate.now(), "MASCULINO");
        Funcionario funcionario = FuncionarioMapper.paraFuncionario(criarDto);

        assertNotNull(funcionario);
        assertEquals(criarDto.getNome(), funcionario.getNome());
        assertEquals(criarDto.getCpf(), funcionario.getCpf());
        assertEquals(criarDto.getDataNascimento(), funcionario.getDataNascimento());
    }

    @Test
    public void testParaDto() {
        Funcionario funcionario = new Funcionario(1L, "Maria", "09876543210", LocalDate.now(), Funcionario.Status.ATIVO, Funcionario.Sexo.FEMININO);
        FuncionarioRespostaDto respostaDto = FuncionarioMapper.paraDto(funcionario);

        assertNotNull(respostaDto);
        assertEquals(funcionario.getNome(), respostaDto.getNome());
        assertEquals(funcionario.getCpf(), respostaDto.getCpf());
        assertEquals(funcionario.getDataNascimento(), respostaDto.getDataNascimento());
        assertEquals(funcionario.getStatus(), respostaDto.getStatus());
        assertEquals(funcionario.getSexo(), respostaDto.getSexo());
    }

    @Test
    public void testAtualizarFuncionario() {
        Funcionario funcionario = new Funcionario(1L, "Pedro", "12345678900", LocalDate.of(2000, 1, 1), Funcionario.Status.ATIVO, Funcionario.Sexo.MASCULINO);
        FuncionarioCriarDto atualizarDto = new FuncionarioCriarDto("João", "09876543211", LocalDate.of(1995, 5, 10), "MASCULINO");

        Funcionario atualizado = FuncionarioMapper.atualizarFuncionario(funcionario, atualizarDto);

        assertNotNull(atualizado);
        assertEquals(atualizarDto.getNome(), atualizado.getNome());
        assertEquals(atualizarDto.getCpf(), atualizado.getCpf());
        assertEquals(atualizarDto.getDataNascimento(), atualizado.getDataNascimento());
        assertEquals(Funcionario.Sexo.MASCULINO, atualizado.getSexo());
        assertEquals(funcionario.getId(), atualizado.getId());
        assertEquals(funcionario.getStatus(), atualizado.getStatus());

    }
}
