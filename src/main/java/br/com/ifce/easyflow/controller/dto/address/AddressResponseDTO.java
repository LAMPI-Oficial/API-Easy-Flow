package br.com.ifce.easyflow.controller.dto.address;

import br.com.ifce.easyflow.model.Address;
import br.com.ifce.easyflow.model.enums.StateEnum;
import io.swagger.annotations.ApiModelProperty;

public class AddressResponseDTO {
    @ApiModelProperty(value = "Person's municipality", example = "123456")
    private String municipality;

    @ApiModelProperty(value = "Person's street", example = "123456")
    private String street;

    @ApiModelProperty(value = "Person's number", example = "123456")
    private String number;

    @ApiModelProperty(value = "Person's complement", example = "123456")
    private String complement;

    @ApiModelProperty(value = "Person's neighborhood", example = "123456")
    private String neighborhood;

    @ApiModelProperty(value = "Person's stateEnum", example = "123456")
    private StateEnum stateEnum;

    public AddressResponseDTO(){

    }

    public AddressResponseDTO(Address address){
        this.municipality = address.getMunicipality();
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.complement = address.getComplement();
        this.neighborhood = address.getNeighborhood();
        this.stateEnum = address.getStateEnum();
    }
}
