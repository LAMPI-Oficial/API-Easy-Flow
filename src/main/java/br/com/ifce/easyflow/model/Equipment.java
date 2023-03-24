package br.com.ifce.easyflow.model;

import br.com.ifce.easyflow.model.enums.EquipmentAvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor @NoArgsConstructor @Builder
@Entity
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tombo;

    private String brand;

    private String name;

    @Column(length = 40)
    private String ramMemory;

    @Column(length = 80)
    private String processor;

    @Column(length = 80)
    private String storageMemory;

    @Enumerated(EnumType.STRING)
    private EquipmentAvailabilityStatus equipmentStatus;
}
