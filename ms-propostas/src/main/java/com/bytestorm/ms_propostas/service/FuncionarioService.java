package com.bytestorm.ms_propostas.service;

import com.bytestorm.ms_propostas.entity.Funcionario;
import com.bytestorm.ms_propostas.exception.ErroComunicacaoMicroservicesException;
import com.bytestorm.ms_propostas.exception.FuncionarioNaoEncontradoException;
import com.bytestorm.ms_propostas.web.clients.FuncionarioFeign;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FuncionarioService {

    private final FuncionarioFeign funcionarioFeign;

    public FuncionarioService(FuncionarioFeign funcionarioFeign) {
        this.funcionarioFeign = funcionarioFeign;
    }

    public boolean funcionarioEhAtivo(Long funcionarioId){
        try {
            ResponseEntity<Funcionario> dadosFuncionarioResponse = funcionarioFeign.buscarFuncionarioPorId(funcionarioId);
            if (dadosFuncionarioResponse.getBody().getStatus() == Funcionario.Status.ATIVO)
                return true;
            else
                return false;
        } catch (FeignException.FeignClientException e){
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status){
                throw new FuncionarioNaoEncontradoException("Funcionario com id '"+ funcionarioId +"' não encontrado!");
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
        }
    }
}
