package com.bytestorm.ms_propostas.exception;

public class VotoJaExisteException extends RuntimeException{

    public VotoJaExisteException(String mensagem) {
        super(mensagem);
    }
}
