package br.com.ifce.easyflow.controller.dto.permission;

import br.com.ifce.easyflow.model.Permission;
import br.com.ifce.easyflow.model.enums.PermissionTypeEnum;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter @Setter
public class PermissionUpdateDTO {
    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty @NotNull
    private PermissionTypeEnum type;

    public Permission toPermission(Long id){
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        permission.setType(this.type);

        return permission;
    }
}
