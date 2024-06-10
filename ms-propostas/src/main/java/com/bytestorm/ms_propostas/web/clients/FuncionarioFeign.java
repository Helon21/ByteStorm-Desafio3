package com.bytestorm.ms_propostas.web.clients;

import com.bytestorm.ms_propostas.entity.Funcionario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "msfuncionarios", path = "funcionarios")
public interface FuncionarioFeign {

    @GetMapping(value = "id/{id}")
    ResponseEntity<Funcionario> buscarFuncionarioPorId(@PathVariable("id") Long id);

}
