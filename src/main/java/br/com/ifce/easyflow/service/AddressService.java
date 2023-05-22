package br.com.ifce.easyflow.service;


import br.com.ifce.easyflow.controller.dto.address.AddressRequestDTO;
import br.com.ifce.easyflow.controller.dto.address.AddressUpdateDTO;
import br.com.ifce.easyflow.model.Address;
import br.com.ifce.easyflow.repository.AddressRepository;
import br.com.ifce.easyflow.service.exceptions.ConflictException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final PersonService personService;

    public AddressService(AddressRepository addressRepository, PersonService personService) {
        this.addressRepository = addressRepository;
        this.personService = personService;
    }

    @Transactional
    public Address save(Address address) {
        return this.addressRepository.save(address);
    }

    public boolean findByPersonId(Long address_id){
        Optional<Address> exist = this.addressRepository.findByPersonId(address_id);
        return exist.isPresent();
    }


    public List<Address> search() {
        return this.addressRepository.findAll();
    }

    public Address searchByID(Long id) {
        return this.addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No address was found with given id."));
    }


    @Transactional
    public Address update(Long id, AddressUpdateDTO requestDTO) {
        Address address = this.searchByID(id);

        address.setComplement(requestDTO.getComplement());
        address.setMunicipality(requestDTO.getMunicipality());
        address.setNeighborhood(requestDTO.getNeighborhood());
        address.setNumber(requestDTO.getNumber());
        address.setStateEnum(requestDTO.getStateEnum());
        address.setStreet(requestDTO.getStreet());

        return this.save(address);
    }

    @Transactional
    public Boolean delete(Long id) {
        Address address = this.searchByID(id);

        if (address != null) {
            this.addressRepository.delete(address);
            return true;
        }

        return false;
    }

    private Address fillUpdateAddress(Address oldAddress, Address newAddress) {
        newAddress.setMunicipality(oldAddress.getMunicipality());
        newAddress.setNeighborhood(oldAddress.getNeighborhood());
        newAddress.setComplement(oldAddress.getComplement());
        newAddress.setNumber(oldAddress.getNumber());
        newAddress.setStreet(oldAddress.getStreet());
        newAddress.setStateEnum(oldAddress.getStateEnum());
        newAddress.setPerson(oldAddress.getPerson());
        return newAddress;
    }


    public boolean existsByID(Long id) {
        Optional<Address> exist = this.addressRepository.findById(id);
        return exist.isPresent();
    }

    public Address createAddress(AddressRequestDTO addressRequestDTO) {
        if(findByPersonId(addressRequestDTO.getPerson_id())){
            throw new ConflictException("Usuário com endereço cadastro.");
        }
        Address address = new Address();
        address.setComplement(addressRequestDTO.getComplement());
        address.setMunicipality(addressRequestDTO.getMunicipality());
        address.setNeighborhood(addressRequestDTO.getNeighborhood());
        address.setNumber(addressRequestDTO.getNumber());
        address.setStreet(addressRequestDTO.getStreet());
        address.setStateEnum(addressRequestDTO.getStateEnum());
        address.setPerson(personService.findById(addressRequestDTO.getPerson_id()));
        this.save(address);
        return address;
    }
}
