package com.bytestorm.ms_propostas.service;

import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.entity.Voto;
import com.bytestorm.ms_propostas.exception.*;
import com.bytestorm.ms_propostas.repository.PropostaRepository;
import com.bytestorm.ms_propostas.repository.VotoRepository;
import com.bytestorm.ms_propostas.web.dto.IniciarVotacaoDTO;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import com.bytestorm.ms_propostas.web.dto.ResultadoDTO;
import com.bytestorm.ms_propostas.web.dto.mapper.PropostaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PropostaService {

    private final PropostaRepository propostaRepository;
    private final VotoRepository votoRepository;
    private final FuncionarioService funcionarioService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final KafkaTemplate<String, Serializable> kafkaTemplate;

    public PropostaService(PropostaRepository propostaRepository, VotoRepository votoRepository, FuncionarioService funcionarioService, KafkaTemplate<String, Serializable> kafkaTemplate) {
        this.propostaRepository = propostaRepository;
        this.votoRepository = votoRepository;
        this.funcionarioService = funcionarioService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(readOnly = true)
    public List<Proposta> buscarTodasPropostas() {
        return propostaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Proposta buscarPropostaPorId(Long id) {
        return propostaRepository.findById(id)
                .orElseThrow(() -> new PropostaNaoEncontradaException("Proposta com o id '" + id + "' não encontrada!"));
    }

    @Transactional
    public Proposta criarProposta(PropostaCriarDTO propostaDto) {
        if (!funcionarioService.funcionarioEhAtivo(propostaDto.getFuncionarioId())) {
            throw new FuncionarioInativoException("Funcionario com id '"+ propostaDto.getFuncionarioId() +"' está inativado e não pode criar propostas");
        }

        Proposta proposta = PropostaMapper.paraProposta(propostaDto);
        proposta.setStatus(Proposta.Status.ATIVO);

        return propostaRepository.save(proposta);
    }

    @Transactional
    public Proposta inativarProposta(Long id) {
        Proposta proposta = buscarPropostaPorId(id);

        if (proposta.getStatus() == Proposta.Status.ATIVO || proposta.getStatus() == Proposta.Status.INATIVO) {
            proposta.setStatus(Proposta.Status.INATIVO);
            return propostaRepository.save(proposta);
        } else {
            throw new PropostaNaoPodeSerInativadaException("Esta proposta está com status: '" + proposta.getStatus() + "' e por isso não pode ser inativada");
        }
    }

    @Transactional
    public Proposta iniciarVotacao(Long id, IniciarVotacaoDTO dto) {
        Proposta proposta = buscarPropostaPorId(id);
        validarPropostaParaVotacao(proposta);

        Integer tempoVotacaoMin = dto.getTempoVotacaoMinutos() != null ? dto.getTempoVotacaoMinutos() : 1;
        configurarVotacao(proposta, tempoVotacaoMin);

        scheduler.schedule(() -> encerrarVotacao(proposta.getId()), tempoVotacaoMin, TimeUnit.MINUTES);

        return propostaRepository.save(proposta);
    }

    @Transactional
    private void encerrarVotacao(Long propostaId) {
        Proposta proposta = buscarPropostaPorId(propostaId);

        if (proposta.getStatus() == Proposta.Status.EM_VOTACAO) {
            proposta.setStatus(Proposta.Status.VOTACAO_ENCERRADA);
            propostaRepository.save(proposta);

            ResultadoDTO dto = criarResultadoDTO(proposta);
            kafkaTemplate.send("resultado-topico", dto);
        }
    }

    private void validarPropostaParaVotacao(Proposta proposta) {
        if (proposta.getStatus() != Proposta.Status.ATIVO) {
            throw new PropostaNaoPodeEntrarEmVotacao(
                    "Esta proposta está com status: '" + proposta.getStatus() + "' e por isso não pode entrar em votação");
        }
    }

    private ResultadoDTO criarResultadoDTO(Proposta proposta) {
        Integer qtdAprovado = votoRepository.countAprovadosByPropostaId(proposta.getId());
        Integer qtdRejeitado = votoRepository.countRejeitadosByPropostaId(proposta.getId());
        String status = (qtdAprovado > qtdRejeitado) ? "APROVADO" : "REJEITADO";

        return new ResultadoDTO(
                proposta.getId(),
                proposta.getTitulo(),
                proposta.getDescricao(),
                proposta.getFuncionarioId(),
                proposta.getDataVotacao(),
                qtdAprovado,
                qtdRejeitado,
                status
        );
    }

    private void configurarVotacao(Proposta proposta, Integer tempoVotacaoMin) {
        proposta.setStatus(Proposta.Status.EM_VOTACAO);
        proposta.setTempoVotacaoMinutos(tempoVotacaoMin);
        proposta.setDataVotacao(LocalDateTime.now());
    }
}