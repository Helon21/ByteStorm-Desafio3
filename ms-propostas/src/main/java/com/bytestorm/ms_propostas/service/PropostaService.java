package com.bytestorm.ms_propostas.service;

import com.bytestorm.ms_propostas.entity.Funcionario;
import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.exception.*;
import com.bytestorm.ms_propostas.repository.PropostaRepository;
import com.bytestorm.ms_propostas.web.clients.FuncionarioFeign;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import com.bytestorm.ms_propostas.web.dto.mapper.PropostaMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class PropostaService {

    private final PropostaRepository propostaRepository;
    private final FuncionarioFeign funcionarioFeign;

    public PropostaService(PropostaRepository propostaRepository, FuncionarioFeign funcionarioFeign) {
        this.propostaRepository = propostaRepository;
        this.funcionarioFeign = funcionarioFeign;
    }

    public List<Proposta> buscarTodasPropostas() {
        return propostaRepository.findAll();
    }


    @Transactional
    public Proposta criarProposta(PropostaCriarDTO propostaDto) {
        if (!funcionarioEhAtivo(propostaDto)) {
            throw new FuncionarioInativoException("Funcionario com id '"+ propostaDto.getFuncionarioId() +"' está inativado e não pode criar propostas");
        }

        Proposta proposta = PropostaMapper.paraProposta(propostaDto);
        proposta.setAtivo(true);

        return propostaRepository.save(proposta);
    }

    public Proposta inativarProposta(Long id) {
        Proposta proposta = propostaRepository.findById(id).orElseThrow(
                () -> new PropostaNaoEncontradaException("Proposta não encontrada ou inexistente, verifique se o id digitado está correto")
        );

        if (proposta.getAtivo()) {
            proposta.setAtivo(false);
            return propostaRepository.save(proposta);
        } else {
            throw new PropostaInativaException("Esta proposta já está inativa");
        }
    }

    private boolean funcionarioEhAtivo(PropostaCriarDTO propostaDto){
        try {
            ResponseEntity<Funcionario> dadosFuncionarioResponse = funcionarioFeign.buscarFuncionarioPorId(propostaDto.getFuncionarioId());
            log.info("Status do " + propostaDto.getFuncionarioId() + ": "+ dadosFuncionarioResponse.getBody().getStatus());
            if (dadosFuncionarioResponse.getBody().getStatus() == Funcionario.Status.ATIVO)
                return true;
            else
                return false;
        } catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new FuncionarioNaoEncontradoException("Funcionario com id '"+ propostaDto.getFuncionarioId() +"' não encontrado!");
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
}