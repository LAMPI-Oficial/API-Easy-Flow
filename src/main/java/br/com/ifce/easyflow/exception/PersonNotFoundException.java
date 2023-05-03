package br.com.ifce.easyflow.exception;

public class PersonNotFoundException extends RuntimeException{

    public PersonNotFoundException(){
        super("The person was not found in the database," +
                " please check the registered persons.");
    }
}
