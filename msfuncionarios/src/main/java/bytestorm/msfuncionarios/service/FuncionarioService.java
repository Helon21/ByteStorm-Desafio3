package bytestorm.msfuncionarios.service;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.exceptions.CpfRepetidoException;
import bytestorm.msfuncionarios.exceptions.FuncionarioNaoEncontradoException;
import bytestorm.msfuncionarios.repository.FuncionarioRepository;
import bytestorm.msfuncionarios.web.dto.FuncionarioCriarDto;
import bytestorm.msfuncionarios.web.dto.mapper.FuncionarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    @Transactional
    public Funcionario salvar(FuncionarioCriarDto funcionarioDto) {
        Funcionario funcionario = FuncionarioMapper.paraFuncionario(funcionarioDto);
        funcionario.setStatus(Funcionario.Status.ATIVO);

        Optional<Funcionario> funcionarioRepetido = funcionarioRepository.findByCpf(funcionario.getCpf());
        if (funcionarioRepetido.isPresent()) {
            throw new CpfRepetidoException("Funcionario com cpf " + funcionario.getCpf() + " já cadastrado");
        }

        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public Funcionario alterar(Long id, FuncionarioCriarDto funcionarioCriarDto) {
        Funcionario funcionarioExistente = funcionarioRepository.findById(id)
                .orElseThrow(() -> new FuncionarioNaoEncontradoException("Funcionário com o id '" + id + "' não encontrado"));

        Optional<Funcionario> funcionarioRepetido = funcionarioRepository.findByCpf(funcionarioCriarDto.getCpf());
        if (funcionarioRepetido.isPresent() && !funcionarioRepetido.get().getId().equals(id)) {
            throw new CpfRepetidoException("Funcionário com cpf " + funcionarioCriarDto.getCpf() + " já cadastrado");
        }

        Funcionario funcionario = FuncionarioMapper.atualizarFuncionario(funcionarioExistente, funcionarioCriarDto);
        return funcionarioRepository.save(funcionario);
    }

}