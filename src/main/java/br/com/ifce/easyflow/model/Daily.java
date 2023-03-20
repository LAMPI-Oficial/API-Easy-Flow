package br.com.ifce.easyflow.model;

import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Daily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_id")
    private Long id;


    @Column(name = "daily_task_status")
    @Enumerated(EnumType.STRING)
    private DailyTaskStatusEnum dailyTaskStatusEnum;

    @Column(name = "what_was_done_today_menssage")
    private String WhatWasDoneTodayMessage;

    @Column(name = "any_questions_message")
    private String AnyQuestionsMessage;

    @Column(name = "feedback_message")
    private String FeedbackMessage;

    @Column(name = "date")
    private LocalDateTime localDateTime;

    @JoinColumn
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "daily")
    private Person person;





}
