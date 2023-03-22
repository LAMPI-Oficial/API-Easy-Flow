package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.Daily.DailyRequestSaveDTO;
import br.com.ifce.easyflow.controller.dto.Daily.DailyRequestUpdateDTO;
import br.com.ifce.easyflow.controller.dto.Daily.DailyResponseDTO;
import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
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

    public Page<Daily> listByPersonId(Long id, Pageable pageable) {
        return this.dailyRepository.findByPersonId(id, pageable);
    }

    @Transactional
    public DailyResponseDTO save(DailyRequestSaveDTO dailyRequestSaveDTO) {
       Person person = personRepository.findById(dailyRequestSaveDTO.getPersonId()).orElseThrow();

       Daily daily = Daily.builder()
               .dailyTaskStatusEnum(DailyTaskStatusEnum.IN_PROGRESS)
               .whatWasDoneTodayMessage(dailyRequestSaveDTO.getWhatWasDoneTodayMessage())
               .anyQuestionsMessage(dailyRequestSaveDTO.getAnyQuestionsMessage())
               .feedbackMessage(dailyRequestSaveDTO.getFeedbackMessage())
               .date(dailyRequestSaveDTO.getDate())
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
        daily.setFeedbackMessage(dailyRequestUpdateDTO.getFeedbackMessage());
        daily.setAnyQuestionsMessage(dailyRequestUpdateDTO.getAnyQuestionsMessage());
        daily.setWhatWasDoneTodayMessage(dailyRequestUpdateDTO.getWhatWasDoneTodayMessage());
        return daily;
    }



}