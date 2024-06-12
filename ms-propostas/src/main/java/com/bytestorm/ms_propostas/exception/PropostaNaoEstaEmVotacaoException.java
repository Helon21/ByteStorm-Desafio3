package com.bytestorm.ms_propostas.exception;

public class PropostaNaoEstaEmVotacaoException extends RuntimeException{

    public PropostaNaoEstaEmVotacaoException(String mensagem) {
        super(mensagem);
    }
}
