package br.com.ifce.easyflow.model;

import br.com.ifce.easyflow.model.enums.ScheduleRequestStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "schedule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shiftSchedule;

    private String day;

    @OneToOne
    @JoinColumn(name = "table_id")
    private LabTable table;

    @Enumerated(EnumType.STRING)
    private ScheduleRequestStatus status;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
