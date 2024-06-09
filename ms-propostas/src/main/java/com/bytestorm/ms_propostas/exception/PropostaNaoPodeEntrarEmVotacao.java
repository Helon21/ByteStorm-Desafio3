package com.bytestorm.ms_propostas.exception;

public class PropostaNaoPodeEntrarEmVotacao extends RuntimeException{

    public PropostaNaoPodeEntrarEmVotacao(String mensagem) {
        super(mensagem);
    }
}
