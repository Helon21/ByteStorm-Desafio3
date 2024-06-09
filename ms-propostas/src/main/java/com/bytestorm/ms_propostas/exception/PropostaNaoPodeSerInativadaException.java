package com.bytestorm.ms_propostas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_MODIFIED)
public class PropostaNaoPodeSerInativadaException extends RuntimeException {

    public PropostaNaoPodeSerInativadaException(String msg) {
        super(msg);
    }
}
