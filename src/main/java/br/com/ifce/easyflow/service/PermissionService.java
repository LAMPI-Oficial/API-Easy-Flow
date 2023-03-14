package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.repository.PermissionRepository;
import br.com.ifce.easyflow.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public Permission save(Permission permission){
        return this.permissionRepository.save(permission);
    }

    public List<Permission> search(){
        return this.permissionRepository.findAll();
    }

    public Optional<Permission> searchByID(Long id){
        return this.permissionRepository.findById(id);
    }

    @Transactional
    public Optional<Permission> update(Permission newPermission){
        Optional<Permission> oldPermission = this.searchByID(newPermission.getId());

        return oldPermission.isPresent()
                ? Optional.of(this.save(newPermission))
                : Optional.empty();
    }

    @Transactional
    public Boolean delete(Long id){
        Optional<Permission> permission = this.searchByID(id);

        if(permission.isPresent()){
            this.permissionRepository.delete(permission.get());
            return true;
        }

        return false;
    }

}
