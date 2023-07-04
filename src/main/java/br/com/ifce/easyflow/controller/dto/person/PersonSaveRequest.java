package br.com.ifce.easyflow.controller.dto.person;

import javax.validation.Valid;

import br.com.ifce.easyflow.controller.dto.address.AddressRequestDTO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PersonSaveRequest {
    @Valid
    private PersonCreateDTO personCreateDTO;

    @Valid
    private AddressRequestDTO addressRequestDTO;
}
