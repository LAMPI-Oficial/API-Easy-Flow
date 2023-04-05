package br.com.ifce.easyflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "lab_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long number;

//    @OneToMany(mappedBy = "table", targetEntity = ReservedTables.class, cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<ReservedTables> reservedTables;
}
