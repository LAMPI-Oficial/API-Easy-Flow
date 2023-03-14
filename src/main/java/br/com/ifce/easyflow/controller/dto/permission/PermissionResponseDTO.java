package br.com.ifce.easyflow.controller.dto.permission;

import br.com.ifce.easyflow.model.Permission;
import br.com.ifce.easyflow.model.enums.PermissionTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PermissionResponseDTO {

    private Long id;
    private String name;
    private PermissionTypeEnum type;

    public PermissionResponseDTO(Permission permission){
        this.id = permission.getId();
        this.name = permission.getName();
        this.type = permission.getType();
    }
}
