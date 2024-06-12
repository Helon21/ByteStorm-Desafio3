package bytestorm.msfuncionarios.web.dto.mapper;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.web.dto.FuncionarioRespostaDto;
import bytestorm.msfuncionarios.web.dto.FuncionarioCriarDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FuncionarioMapper {

    public static Funcionario paraFuncionario(FuncionarioCriarDto dto) {
        return new ModelMapper().map(dto, Funcionario.class);
    }

    public static FuncionarioRespostaDto paraDto(Funcionario funcionario) {
        return new ModelMapper().map(funcionario, FuncionarioRespostaDto.class);
    }

    public static Funcionario atualizarFuncionario(Funcionario funcionario, FuncionarioCriarDto funcionarioDto) {
        funcionario.setNome(funcionarioDto.getNome());
        funcionario.setCpf(funcionarioDto.getCpf());
        funcionario.setDataNascimento(funcionarioDto.getDataNascimento());
        funcionario.setSexo(Funcionario.Sexo.valueOf(funcionarioDto.getSexo()));
        return funcionario;
    }
    
}
