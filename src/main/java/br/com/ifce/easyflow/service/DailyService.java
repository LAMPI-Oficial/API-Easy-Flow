package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.daily.DailyRequestSaveDTO;
import br.com.ifce.easyflow.controller.dto.daily.DailyRequestSaveFeedbackDTO;
import br.com.ifce.easyflow.controller.dto.daily.DailyRequestUpdateDTO;
import br.com.ifce.easyflow.controller.dto.daily.DailyResponseDTO;
import br.com.ifce.easyflow.exception.PersonNotFoundException;
import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.repository.DailyRepository;
import br.com.ifce.easyflow.repository.PersonRepository;
import br.com.ifce.easyflow.service.exceptions.BadRequestException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class DailyService {
    private final DailyRepository dailyRepository;
    private final PersonRepository personRepository;

    public Page<DailyResponseDTO> listAll(Pageable pageable) {

        return dailyRepository.findAll(pageable).map(DailyResponseDTO::new);
    }

    public DailyResponseDTO findById(Long id) {
        Daily daily = dailyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No daily found with given id"));

        return new DailyResponseDTO(daily);
    }

    public Page<DailyResponseDTO> listByPersonId(Long id, Pageable pageable) {
        boolean personExists = personRepository.existsById(id);
        if (!personExists) {
            throw new PersonNotFoundException();
        }
        return dailyRepository.findByPersonId(id, pageable).map(DailyResponseDTO::new);
    }

    public Page<DailyResponseDTO> listByDate(String date, Pageable pageable) {
        try {

            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return dailyRepository.findByDate(localDate, pageable).map(DailyResponseDTO::new);

        } catch (DateTimeParseException e) {
            throw new BadRequestException("The date format does not conform to the format: yyyy-MM-dd. " + e.getMessage());
        }

    }

    public Page<DailyResponseDTO> listByPersonIdAndDate(Long id, String date, Pageable pageable) {

        if (!personRepository.existsById(id)) {
            throw new PersonNotFoundException();
        }

        try {

            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            return dailyRepository.findByPersonIdAndDate(id, localDate, pageable).map(DailyResponseDTO::new);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("The date format does not conform to the format: yyyy-MM-dd. " + e.getMessage());
        }
    }

    @Transactional
    public DailyResponseDTO save(DailyRequestSaveDTO dailyRequestSaveDTO) {
        Person person = personRepository.findById(dailyRequestSaveDTO.getPersonId())
                .orElseThrow(PersonNotFoundException::new);

        Daily daily = Daily.builder()
                .dailyTaskStatusEnum(dailyRequestSaveDTO.getDailyTaskStatusEnum())
                .whatWasDoneTodayMessage(dailyRequestSaveDTO.getWhatWasDoneTodayMessage())
                .anyQuestionsMessage(dailyRequestSaveDTO.getAnyQuestionsMessage())
                .date(dailyRequestSaveDTO.getDate())
                .person(person)
                .build();

        return new DailyResponseDTO(dailyRepository.save(daily));


    }

    @Transactional
    public DailyResponseDTO update(Long dailyId, DailyRequestUpdateDTO dailyRequestUpdateDTO) {
        Daily dailySaved = dailyRepository.findById(dailyId)
                .orElseThrow(() -> new ResourceNotFoundException("No daily found with given id"));

        Daily dailyToUpdate = updateDailyWithDailyUpdateDto(dailySaved, dailyRequestUpdateDTO);
        return new DailyResponseDTO(dailyRepository.save(dailyToUpdate));


    }

    @Transactional
    public DailyResponseDTO saveFeedback(Long id, DailyRequestSaveFeedbackDTO dailyRequestSaveFeedbackDTO) {
        Daily dailySaved = dailyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No daily found with given id"));

        dailySaved.setFeedbackMessage(dailyRequestSaveFeedbackDTO.getFeedbackMessage());

        return new DailyResponseDTO(dailyRepository.save(dailySaved));

    }

    @Transactional
    public void delete(Long id) {
        dailyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No daily found with given id"));
        dailyRepository.deleteById(id);
    }


    private Daily updateDailyWithDailyUpdateDto(Daily daily, DailyRequestUpdateDTO dailyRequestUpdateDTO) {
        daily.setAnyQuestionsMessage(dailyRequestUpdateDTO.getAnyQuestionsMessage());
        daily.setWhatWasDoneTodayMessage(dailyRequestUpdateDTO.getWhatWasDoneTodayMessage());
        return daily;
    }

}