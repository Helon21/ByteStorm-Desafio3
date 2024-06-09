package com.bytestorm.ms_propostas.exception;

import lombok.Getter;

public class ErroComunicacaoMicroservicesException extends RuntimeException{

    @Getter
    private Integer status;

    public ErroComunicacaoMicroservicesException(String msg, Integer status) {
        super(msg);
        this.status = status;
    }
}