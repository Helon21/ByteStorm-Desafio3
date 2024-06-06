package com.bytestorm.ms_propostas.service;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.exception.PropostaInativaException;
import com.bytestorm.ms_propostas.exception.PropostaNaoEncontradaException;
import com.bytestorm.ms_propostas.repository.PropostaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropostaService {

    private final PropostaRepository propostaRepository;

    public PropostaService(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    public List<Proposta> buscarTodasPropostas() {
        return propostaRepository.findAll();
    }

    public Proposta criarProposta(Proposta proposta) {
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
}