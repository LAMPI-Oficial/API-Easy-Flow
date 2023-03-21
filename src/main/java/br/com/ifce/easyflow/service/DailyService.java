package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.Daily.DailyRequestSaveDTO;
import br.com.ifce.easyflow.controller.dto.Daily.DailyRequestUpdateDTO;
import br.com.ifce.easyflow.controller.dto.Daily.DailyResponseDTO;
import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.repository.DailyRepository;
import br.com.ifce.easyflow.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DailyService {
    private final  DailyRepository dailyRepository;
    private final PersonRepository personRepository;
    public Page<Daily> listAll(Pageable pageable){
        return this.dailyRepository.findAll(pageable);
    }

    public DailyResponseDTO findById(Long id) {
       Daily daily = dailyRepository.findById(id).orElseThrow();

       return DailyResponseDTO.toResponseDTO(daily);
    }

    @Transactional
    public DailyResponseDTO save(DailyRequestSaveDTO dailyRequestSaveDTO) {
       Person person = personRepository.findById(dailyRequestSaveDTO.getPersonId()).orElseThrow();

       Daily daily = Daily.builder()
               .dailyTaskStatusEnum(dailyRequestSaveDTO.getDailyTaskStatusEnum())
               .WhatWasDoneTodayMessage(dailyRequestSaveDTO.getWhatWasDoneTodayMessage())
               .AnyQuestionsMessage(dailyRequestSaveDTO.getAnyQuestionsMessage())
               .FeedbackMessage(dailyRequestSaveDTO.getFeedbackMessage())
               .localDateTime(dailyRequestSaveDTO.getLocalDateTime())
               .person(person)
               .build();
       return DailyResponseDTO.toResponseDTO(dailyRepository.save(daily));



    }

    @Transactional
    public DailyResponseDTO update(Long dailyId,DailyRequestUpdateDTO dailyRequestUpdateDTO) {
        Daily dailySaved = dailyRepository.findById(dailyId).orElseThrow();

        Daily dailyToUpdate = updateDailyWithDailyUpdateDto(dailySaved, dailyRequestUpdateDTO);
       return DailyResponseDTO.toResponseDTO(dailyRepository.save(dailyToUpdate));


    }

    @Transactional
    public void delete(Long id) {
        dailyRepository.findById(id).orElseThrow();
        dailyRepository.deleteById(id);
    }



    private Daily updateDailyWithDailyUpdateDto(Daily daily, DailyRequestUpdateDTO dailyRequestUpdateDTO){
        daily.setDailyTaskStatusEnum(dailyRequestUpdateDTO.getDailyTaskStatusEnum());
        daily.setFeedbackMessage(dailyRequestUpdateDTO.getFeedbackMessage());
        daily.setAnyQuestionsMessage(dailyRequestUpdateDTO.getAnyQuestionsMessage());
        daily.setWhatWasDoneTodayMessage(dailyRequestUpdateDTO.getWhatWasDoneTodayMessage());
        return daily;
    }


}