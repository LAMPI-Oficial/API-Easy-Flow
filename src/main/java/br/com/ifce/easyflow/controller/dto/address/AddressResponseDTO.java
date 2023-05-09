package br.com.ifce.easyflow.controller.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.ifce.easyflow.controller.dto.person.PersonDTO;
import br.com.ifce.easyflow.model.Address;
import br.com.ifce.easyflow.model.enums.StateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressResponseDTO {
    private Long id;

    @ApiModelProperty(value = "Person's municipality", example = "Caninde")
    private String municipality;

    @ApiModelProperty(value = "Person's street", example = "Romeu Martins")
    private String street;

    @ApiModelProperty(value = "Person's number", example = "123456")
    private String number;

    @ApiModelProperty(value = "Person's complement", example = "Apto")
    private String complement;

    @ApiModelProperty(value = "Person's neighborhood", example = "Vizinho a Caixa")
    private String neighborhood;

    @ApiModelProperty(value = "Person's stateEnum", example = "CEARA")
    private StateEnum stateEnum;

    @JsonProperty("tb_person")
    private PersonDTO personDTO;

    public AddressResponseDTO(){

    }

    public AddressResponseDTO(Address address){
        this.id = address.getId();
        this.municipality = address.getMunicipality();
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.complement = address.getComplement();
        this.neighborhood = address.getNeighborhood();
        this.stateEnum = address.getStateEnum();

        if(address.getPerson() != null){
            this.personDTO = new PersonDTO(address.getPerson());
        }

    }
}
