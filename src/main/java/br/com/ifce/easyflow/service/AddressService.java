package br.com.ifce.easyflow.service;


import br.com.ifce.easyflow.repository.AddressRepository;
import br.com.ifce.easyflow.repository.PermissionRepository;
import br.com.ifce.easyflow.controller.dto.address.AddressRequestDTO;
import br.com.ifce.easyflow.model.Address;
import br.com.ifce.easyflow.model.Permission;
import br.com.ifce.easyflow.model.enums.StateEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService { 

    private final AddressRepository addressRepository;
    private final PersonService personService;

    public AddressService(AddressRepository addressRepository, PersonService personService){
        this.addressRepository = addressRepository;
        this.personService = personService;
    }

    @Transactional
    public Address save(Address address){
        return this.addressRepository.save(address);
    }


    public List<Address> search(){
        return this.addressRepository.findAll();
    }

    public Optional<Address> searchByID(Long id){
        return this.addressRepository.findById(id);
    }


    @Transactional
    public Optional<Address> update(Address newAddress){
        Optional<Address> oldAddress = this.searchByID(newAddress.getId());

        return oldAddress.isPresent()
                ? Optional.of(this.save(this.fillUpdateAddress(oldAddress.get(),newAddress)))
                : Optional.empty();
    }

    @Transactional
    public Boolean delete(Long id){
        Optional<Address> Address = this.searchByID(id);

        if(Address.isPresent()){
            this.addressRepository.delete(Address.get());
            return true;
        }

        return false;
    }

    private Address fillUpdateAddress(Address oldAddress,Address newAddress){
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
        Address address = new Address();
        address.setComplement(addressRequestDTO.getComplement());
        address.setMunicipality(addressRequestDTO.getMunicipality());
        address.setNeighborhood(addressRequestDTO.getNeighborhood());
        address.setNumber(addressRequestDTO.getNumber());
        address.setStreet(addressRequestDTO.getStreet());
        address.setStateEnum(addressRequestDTO.getStateEnum());
        address.setPerson(personService.findById(addressRequestDTO.getPerson_id()).get());
        return address;
    }
}
