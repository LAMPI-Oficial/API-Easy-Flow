package br.com.ifce.easyflow.model;

import br.com.ifce.easyflow.model.enums.StateEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "municipality")
    private String municipality;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private String number;

    @Column(name = "complement")
    private String complement;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private StateEnum stateEnum;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;


}
