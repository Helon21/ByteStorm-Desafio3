package bytestorm.msfuncionarios.service;

import bytestorm.msfuncionarios.entity.Funcionario;
import bytestorm.msfuncionarios.exceptions.CpfRepetidoException;
import bytestorm.msfuncionarios.exceptions.FuncionarioNaoEncontradoException;
import bytestorm.msfuncionarios.repository.FuncionarioRepository;
import bytestorm.msfuncionarios.repository.projection.FuncionarioProjection;
import bytestorm.msfuncionarios.web.dto.FuncionarioCriarDto;
import bytestorm.msfuncionarios.web.dto.mapper.FuncionarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public Funcionario buscarFuncionarioPorId(Long id) {
        return funcionarioRepository.findById(id).orElseThrow(
                () -> new FuncionarioNaoEncontradoException("Funcionário com o id '" + id + "' não encontrado")
        );
    }

    @Transactional(readOnly = true)
    public Funcionario buscarFuncionarioPorCpf(String cpf) {
        return funcionarioRepository.findByCpf(cpf).orElseThrow(
                () -> new FuncionarioNaoEncontradoException("Funcionário com o cpf '" + cpf + "' não encontrado")
        );
    }

    @Transactional(readOnly = true)
    public Page<FuncionarioProjection> getAll(Pageable pageable) {
        return funcionarioRepository.findAllPageable(pageable);
    }
}