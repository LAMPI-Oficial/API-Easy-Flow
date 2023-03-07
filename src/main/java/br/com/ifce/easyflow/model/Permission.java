package br.com.ifce.easyflow.model;

import br.com.ifce.easyflow.model.enums.PermissionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "permission")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Permission implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "permission_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_type")
    private PermissionTypeEnum type;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
