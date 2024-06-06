package com.bytestorm.ms_propostas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PropostaInativaException extends RuntimeException {

    public PropostaInativaException(String msg) {
        super(msg);
    }
}
