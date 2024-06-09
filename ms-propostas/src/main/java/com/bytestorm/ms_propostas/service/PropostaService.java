package com.bytestorm.ms_propostas.service;

import com.bytestorm.ms_propostas.entity.Funcionario;
import com.bytestorm.ms_propostas.entity.Proposta;
import com.bytestorm.ms_propostas.exception.*;
import com.bytestorm.ms_propostas.repository.PropostaRepository;
import com.bytestorm.ms_propostas.web.clients.FuncionarioFeign;
import com.bytestorm.ms_propostas.web.dto.IniciarVotacaoDTO;
import com.bytestorm.ms_propostas.web.dto.PropostaCriarDTO;
import com.bytestorm.ms_propostas.web.dto.mapper.PropostaMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PropostaService {

    private final PropostaRepository propostaRepository;
    private final FuncionarioFeign funcionarioFeign;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PropostaService(PropostaRepository propostaRepository, FuncionarioFeign funcionarioFeign) {
        this.propostaRepository = propostaRepository;
        this.funcionarioFeign = funcionarioFeign;
    }

    @Transactional(readOnly = true)
    public List<Proposta> buscarTodasPropostas() {
        return propostaRepository.findAll();
    }

    @Transactional
    public Proposta criarProposta(PropostaCriarDTO propostaDto) {
        if (!funcionarioEhAtivo(propostaDto)) {
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
        log.info("Votação da proposta com id " + id + " iniciada e será encerrada em " + tempoVotacaoMin + " minutos.");

        return propostaRepository.save(proposta);
    }

    @Transactional
    private void encerrarVotacao(Long propostaId) {
        Proposta proposta = buscarPropostaPorId(propostaId);

        if (proposta.getStatus() == Proposta.Status.EM_VOTACAO) {
            proposta.setStatus(Proposta.Status.VOTACAO_ENCERRADA);
            propostaRepository.save(proposta);
            log.info("Votação da proposta com id " + propostaId + " encerrada.");
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

    private Proposta buscarPropostaPorId(Long id) {
        return propostaRepository.findById(id)
                .orElseThrow(() -> new PropostaNaoEncontradaException("Proposta com o id '" + id + "' não encontrada!"));
    }

    private void validarPropostaParaVotacao(Proposta proposta) {
        if (proposta.getStatus() != Proposta.Status.ATIVO) {
            throw new PropostaNaoPodeEntrarEmVotacao(
                    "Esta proposta está com status: '" + proposta.getStatus() + "' e por isso não pode entrar em votação");
        }
    }

    private void configurarVotacao(Proposta proposta, Integer tempoVotacaoMin) {
        proposta.setStatus(Proposta.Status.EM_VOTACAO);
        proposta.setTempoVotacaoMinutos(tempoVotacaoMin);
        proposta.setDataVotacao(LocalDateTime.now());
    }
}