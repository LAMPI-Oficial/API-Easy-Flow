package br.com.ifce.easyflow.model;

import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Daily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_id")
    private Long id;


    @Column(name = "daily_task_status")
    @Enumerated(EnumType.STRING)
    private DailyTaskStatusEnum dailyTaskStatusEnum;

    @Column(name = "what_was_done_today_message")
    private String whatWasDoneTodayMessage;

    @Column(name = "any_questions_message")
    private String anyQuestionsMessage;

    @Column(name = "feedback_message")
    private String feedbackMessage;

    @Column(name = "date")
    private LocalDate date;

    @JoinColumn(name = "person_id", nullable = false)
    @ManyToOne()
    private Person person;





}
