package br.com.ifce.easyflow.exception;

public class PersonNotFoundException extends RuntimeException{

    public PersonNotFoundException(){
        super("Pessoa não encontrada");
    }
}
