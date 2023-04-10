package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.repository.PermissionRepository;
import br.com.ifce.easyflow.model.Permission;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
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

    public Permission searchByID(Long id){
        return this.permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with given id"));
    }

    @Transactional
    public Permission update(Long id, Permission newPermission){
        Permission oldPermission = this.searchByID(id);

        return this.save(newPermission);
    }

    @Transactional
    public Boolean delete(Long id){
        Permission permission = this.searchByID(id);

        if(permission != null){
            this.permissionRepository.delete(permission);
            return true;
        }
        return false;
    }

}
