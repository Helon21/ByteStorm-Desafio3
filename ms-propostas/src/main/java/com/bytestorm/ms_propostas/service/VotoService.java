package com.bytestorm.ms_propostas.service;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.entity.Voto;
import com.bytestorm.ms_propostas.entity.pk.VotoId;
import com.bytestorm.ms_propostas.exception.FuncionarioInativoException;
import com.bytestorm.ms_propostas.exception.PropostaNaoEstaEmVotacaoException;
import com.bytestorm.ms_propostas.exception.VotoJaExisteException;
import com.bytestorm.ms_propostas.repository.VotoRepository;
import com.bytestorm.ms_propostas.web.dto.VotoCriarDto;
import com.bytestorm.ms_propostas.web.dto.mapper.VotoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final FuncionarioService funcionarioService;
    private final PropostaService propostaService;

    public VotoService(VotoRepository votoRepository, FuncionarioService funcionarioService, PropostaService propostaService) {
        this.votoRepository = votoRepository;
        this.funcionarioService = funcionarioService;
        this.propostaService = propostaService;
    }

    @Transactional
    public Voto votarEmProposta(Long idProposta, VotoCriarDto dto) {
        validarFuncionarioAtivo(dto.getFuncionarioId());

        Proposta proposta = propostaService.buscarPropostaPorId(idProposta);
        validarPropostaParaVotar(proposta);

        Voto.Status status = obterStatusDoVoto(dto.getStatus());
        Voto voto = VotoMapper.paraVoto(dto.getFuncionarioId(), proposta, status);

        validarSeVotoJaExiste(voto.getId());

        log.info("Funcionario com id {} votou {} na proposta de id {}", dto.getFuncionarioId(), dto.getStatus(), idProposta);
        return votoRepository.save(voto);
    }

    private void validarFuncionarioAtivo(Long funcionarioId) {
        if (!funcionarioService.funcionarioEhAtivo(funcionarioId)) {
            throw new FuncionarioInativoException("Funcionario com id '" + funcionarioId + "' está inativado e não pode votar em propostas!");
        }
    }

    private void validarPropostaParaVotar(Proposta proposta) {
        if (proposta.getStatus() != Proposta.Status.EM_VOTACAO) {
            throw new PropostaNaoEstaEmVotacaoException(
                    "Proposta com id '"+ proposta.getId() +"' não está aberta para votação!");
        }
    }

    private void validarSeVotoJaExiste(VotoId id) {
        Optional<Voto> voto = votoRepository.findById(id);
        if (voto.isPresent()) {
            log.warn("Voto já existe para o funcionário com id {} na proposta com id {}", id.getFuncionarioId(), id.getPropostaId());
            throw new VotoJaExisteException("Voto já existe para o funcionário com id '" + id.getFuncionarioId() + "' na proposta com id '" + id.getPropostaId() + "'.");
        }
    }

    private Voto.Status obterStatusDoVoto(String status) {
        try {
            return Voto.Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            return Voto.Status.REJEITADO;
        }
    }
}
