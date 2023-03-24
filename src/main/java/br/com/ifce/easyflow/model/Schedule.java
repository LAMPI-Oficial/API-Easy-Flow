package br.com.ifce.easyflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "schedule")
@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shiftSchedule;

    private String day;

    private int tableNumber;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
