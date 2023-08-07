package br.com.ifce.easyflow.service.daily;

import br.com.ifce.easyflow.controller.dto.daily.*;
import br.com.ifce.easyflow.exception.PersonNotFoundException;
import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.model.Person;
import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import br.com.ifce.easyflow.repository.DailyRepository;
import br.com.ifce.easyflow.repository.PersonRepository;
import br.com.ifce.easyflow.service.DailyService;
import br.com.ifce.easyflow.service.exceptions.BadRequestException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyServiceTest {
    @InjectMocks
    DailyService dailyService;
    @Mock
    DailyRepository dailyRepository;
    @Mock
    PersonRepository personRepository;

    @Test
    void listAll_DailyResponseDTO_WhenSuccessful() {

        List<Daily> dailyList = List.of(createDaily());

        when(dailyRepository.findAll()).thenReturn(dailyList);

        List<DailySimpleResponseDTO> dailyResponseDTOSList = dailyService.listAll();
        Assertions.assertEquals(dailyList.get(0).getId(), dailyResponseDTOSList.get(0).getId());
        Assertions.assertEquals(dailyList.get(0).getPerson().getId(), dailyResponseDTOSList.get(0).getPersonId());
        Assertions.assertNotNull(dailyResponseDTOSList.get(0).getId());
        verify(dailyRepository).findAll();
    }

    @Test
    void listAll_DailyResponseDTO_Return_EmptyPage_WhenSuccessful() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Daily> dailyList = new ArrayList<>();


        when(dailyRepository.findAll()).thenReturn(dailyList);

        List<DailySimpleResponseDTO> dailyResponseDto = dailyService.listAll();

        Assertions.assertTrue(dailyResponseDto.isEmpty());
        verify(dailyRepository).findAll();
        verifyNoMoreInteractions(dailyRepository);

    }


    @Test
    void findById_DailyResponseDTO_WhenSuccessful() {
        Daily daily = createDaily();
        when(dailyRepository.findById(anyLong())).thenReturn(Optional.of(daily));

        DailyResponseDTO dailyResponseDTO = dailyService.findById(1L);

        Assertions.assertEquals(daily.getId(), dailyResponseDTO.getId());
        Assertions.assertEquals(daily.getPerson().getId(), dailyResponseDTO.getPerson().getId());
        Assertions.assertEquals(1L, dailyResponseDTO.getId());
        verify(dailyRepository).findById(1L);
        verifyNoMoreInteractions(dailyRepository);

    }

    @Test
    void findById_Throws_ResourceNotFoundException_WhenDailyNotFound() {

        when(dailyRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> dailyService.findById(anyLong()));

        Assertions.assertTrue(resourceNotFoundException.getMessage().contains("No daily found with given id"));
    }

    @Test
    void listByPersonId_Return_PageOfDailyResponseDTO_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Daily> dailyList = List.of(createDaily());

        when(personRepository.existsById(anyLong())).thenReturn(true);
        when(dailyRepository.findByPersonId(1L, pageable)).thenReturn(dailyList);

        List<DailyResponseDTO> dailyResponseDTOList = dailyService.listByPersonId(1L, pageable);

        Assertions.assertEquals(dailyList.get(0).getId(), dailyResponseDTOList.get(0).getId());
        Assertions.assertEquals(dailyList.get(0).getPerson().getId(), dailyResponseDTOList.get(0).getPerson().getId());
        verify(dailyRepository).findByPersonId(1L, pageable);

    }

    @Test
    void listByPersonId_Throws_PersonNotFoundException_WhenPersonNotFound() {
        PageRequest pageable = PageRequest.of(0, 5);
        when(personRepository.existsById(anyLong())).thenReturn(false);
        PersonNotFoundException personNotFoundException = Assertions.assertThrows(PersonNotFoundException.class,
                () -> dailyService.listByPersonId(anyLong(), pageable));

        Assertions.assertTrue(personNotFoundException.getMessage().contains("The person was not found in the database, please check the registered persons."));
        verifyNoInteractions(dailyRepository);
    }

    @Test
    void listByDate_PageOfDailyResponseDTO_WhenSuccessful() {
        List<Daily> dailyList = List.of(createDaily());
        Pageable pageable = PageRequest.of(0, 5);
        LocalDate date = LocalDate.parse("2023-05-25", DateTimeFormatter.ISO_DATE);
        when(dailyRepository.findByDate(date, pageable)).thenReturn(dailyList);

        List<DailyResponseDTO> dailyResponseDTOList = dailyService.listByDate("2023-05-25", pageable);

        Assertions.assertEquals(dailyList.get(0).getId(), dailyResponseDTOList.get(0).getId());
        Assertions.assertEquals(dailyList.get(0).getDate(), dailyResponseDTOList.get(0).getDate());

        verify(dailyRepository).findByDate(date, pageable);
    }

    @Test
    void listByDate_Return_PageEmptyOfDailyResponseDTO_WhenNoDailyToBeFound() {
        List<Daily> dailyList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 5);
        LocalDate date = LocalDate.parse("2023-05-25", DateTimeFormatter.ISO_DATE);
        when(dailyRepository.findByDate(date, pageable)).thenReturn(dailyList);

        List<DailyResponseDTO> dailyResponseDTOList = dailyService.listByDate("2023-05-25", pageable).stream().toList();

        Assertions.assertTrue(dailyResponseDTOList.isEmpty());
        verify(dailyRepository).findByDate(date, pageable);
    }

    @Test
    void listByDate_Throws_BadRequestException_WhenUnableToParseDate() {
        Pageable pageable = PageRequest.of(0, 5);
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> dailyService.listByDate("21-52-2000", pageable));
        Assertions.assertTrue(badRequestException.getMessage().contains("The date format does not conform to the format: yyyy-MM-dd. "));

        verifyNoInteractions(dailyRepository);
    }

    @Test
    void listByPersonIdAndDate_PageOfDailyResponseDTO_WhenSuccessful() {
        List<Daily> dailyList = List.of(createDaily());
        Pageable pageable = PageRequest.of(0, 5);
        LocalDate date = LocalDate.parse("2023-05-25", DateTimeFormatter.ISO_DATE);

        when(personRepository.existsById(anyLong())).thenReturn(true);
        when(dailyRepository.findByPersonIdAndDate(1L, date, pageable)).thenReturn(dailyList);

        List<DailyResponseDTO> dailyResponseDTOList = dailyService.listByPersonIdAndDate(1L, "2023-05-25", pageable).stream().toList();

        Assertions.assertEquals(dailyList.get(0).getDate(), dailyResponseDTOList.get(0).getDate());
        Assertions.assertEquals(dailyList.get(0).getId(), dailyResponseDTOList.get(0).getId());
        verify(dailyRepository).findByPersonIdAndDate(1L, date, pageable);


    }

    @Test
    void listByPersonIdAndDate_EmptyPageOfDailyResponseDTO_WhenNoDailyToBeFound() {
        List<Daily> dailyList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 5);
        LocalDate date = LocalDate.parse("2023-05-25", DateTimeFormatter.ISO_DATE);

        when(personRepository.existsById(anyLong())).thenReturn(true);
        when(dailyRepository.findByPersonIdAndDate(1L, date, pageable)).thenReturn(dailyList);

        List<DailyResponseDTO> dailyResponseDTOList = dailyService.listByPersonIdAndDate(1L, "2023-05-25", pageable).stream().toList();

        Assertions.assertTrue(dailyResponseDTOList.isEmpty());
        verify(dailyRepository).findByPersonIdAndDate(1L, date, pageable);


    }

    @Test
    void listByPersonIdAndDate_Throws_PersonNotFoundException_WhenPersonNotFound() {
        Pageable pageable = PageRequest.of(0, 5);
        when(personRepository.existsById(anyLong())).thenReturn(false);

        PersonNotFoundException personNotFoundException = Assertions.assertThrows(PersonNotFoundException.class,
                () -> dailyService.listByPersonIdAndDate(1L, "2023-05-25", pageable));

        Assertions.assertTrue(personNotFoundException.getMessage().contains("The person was not found in the database, please check the registered persons."));

        verifyNoInteractions(dailyRepository);
    }

    @Test
    void listByPersonIdAndDate_Throws_BadRequestException_WhenUnableToParseDate() {
        Pageable pageable = PageRequest.of(0, 5);
        when(personRepository.existsById(anyLong())).thenReturn(true);
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> dailyService.listByPersonIdAndDate(1L, "21-52-2000", pageable));
        Assertions.assertTrue(badRequestException.getMessage().contains("The date format does not conform to the format: yyyy-MM-dd. "));

        verifyNoInteractions(dailyRepository);
    }

    @Test
    void save_DailyResponseDTO_WhenSuccessful() {
        Daily daily = createDaily();
        DailyRequestSaveDTO dailyRequestSaveDTO = createDailyRequestSaveDTO();
        Person person = new Person();
        person.setId(1L);
        person.setName("vfvfv");

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        when(dailyRepository.save(any(Daily.class))).thenReturn(daily);

        DailyResponseDTO dailyResponseDTO = dailyService.save(dailyRequestSaveDTO);

        Assertions.assertNotNull(dailyResponseDTO.getId());
        Assertions.assertEquals(daily.getId(), dailyResponseDTO.getId());
        Assertions.assertEquals(daily.getDate(), dailyResponseDTO.getDate());
        Assertions.assertEquals(daily.getDailyTaskStatusEnum(), dailyResponseDTO.getDailyTaskStatusEnum());
        Assertions.assertEquals(daily.getPerson().getId(), dailyResponseDTO.getPerson().getId());
        Assertions.assertEquals(daily.getAnyQuestionsMessage(), dailyResponseDTO.getAnyQuestionsMessage());
        Assertions.assertEquals(daily.getWhatWasDoneTodayMessage(), dailyResponseDTO.getWhatWasDoneTodayMessage());

        verifyNoMoreInteractions(dailyRepository);
    }

    @Test
    void save_Throws_PersonNotFoundException_WhenPersonNotFound() {

        Person person = new Person();
        person.setId(1L);
        person.setName("vfvfv");
        DailyRequestSaveDTO dailyRequestSaveDTO = createDailyRequestSaveDTO();

        when(personRepository.findById(anyLong())).thenReturn(Optional.empty());
        PersonNotFoundException personNotFoundException = Assertions.assertThrows(PersonNotFoundException.class,
                () -> dailyService.save(dailyRequestSaveDTO));
        Assertions.assertTrue(personNotFoundException.getMessage().contains("The person was not found in the database, please check the registered persons."));

        verifyNoInteractions(dailyRepository);
    }

    @Test
    void update_DailyResponseDTO_WhenSuccessful() {
        Daily daily = createDaily();
        daily.setDailyTaskStatusEnum(DailyTaskStatusEnum.IN_PROGRESS);
        daily.setAnyQuestionsMessage("vybsmjdmudoumudoumdodcdc");
        daily.setWhatWasDoneTodayMessage("vndvsuvdsdvnusmudouc");

        when(dailyRepository.findById(anyLong())).thenReturn(Optional.of(daily));
        when(dailyRepository.save(any(Daily.class))).thenReturn(daily);

        DailyRequestUpdateDTO dailyRequestUpdateDTO = createDailyRequestUpdateDTO();
        DailyResponseDTO dailyResponseDTO = dailyService.update(1L, dailyRequestUpdateDTO);

        Assertions.assertEquals(dailyRequestUpdateDTO.getDailyTaskStatusEnum(), dailyResponseDTO.getDailyTaskStatusEnum());

    }

    @Test
    void update_Throws_ResourceNotFoundException_WhenDailyNotFound() {
        DailyRequestUpdateDTO dailyRequestUpdateDTO = createDailyRequestUpdateDTO();

        when(dailyRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> dailyService.update(1L, dailyRequestUpdateDTO));
        Assertions.assertTrue(resourceNotFoundException.getMessage().contains("No daily found with given id"));

        verify(dailyRepository).findById(1L);
    }

    @Test
    void saveFeedback_DailyResponseDTO_WhenSuccessful() {
        Daily daily = createDaily();
        DailyRequestSaveFeedbackDTO dailyRequestSaveFeedbackDTO = new DailyRequestSaveFeedbackDTO();
        dailyRequestSaveFeedbackDTO.setFeedbackMessage("vffvss");
        Daily dailyToResponse = createDaily();
        dailyToResponse.setFeedbackMessage(dailyRequestSaveFeedbackDTO.getFeedbackMessage());

        when(dailyRepository.findById(anyLong())).thenReturn(Optional.of(daily));
        when(dailyRepository.save(any(Daily.class))).thenReturn(dailyToResponse);

        DailyResponseDTO dailyResponseDTO = dailyService.saveFeedback(1L, dailyRequestSaveFeedbackDTO);

        Assertions.assertEquals(dailyToResponse.getId(), dailyResponseDTO.getId());
        Assertions.assertEquals(dailyToResponse.getFeedbackMessage(), dailyResponseDTO.getFeedbackMessage());

        verify(dailyRepository).findById(1L);
        verifyNoMoreInteractions(dailyRepository);

    }

    @Test
    void saveFeedback_Throws_ResourceNotFoundException_WhenDailyNotFound() {

        DailyRequestSaveFeedbackDTO dailyRequestSaveFeedbackDTO = new DailyRequestSaveFeedbackDTO();
        dailyRequestSaveFeedbackDTO.setFeedbackMessage("vffvss");

        when(dailyRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> dailyService.saveFeedback(1L, dailyRequestSaveFeedbackDTO));
        Assertions.assertTrue(resourceNotFoundException.getMessage().contains("No daily found with given id"));

        verify(dailyRepository).findById(1L);
        verifyNoMoreInteractions(dailyRepository);
    }

    @Test
    void delete_WhenSuccessful() {
        Long id = 1L;
        when(dailyRepository.existsById(id)).thenReturn(true);

        dailyService.delete(id);

        verify(dailyRepository, times(1)).deleteById(id);

    }

    @Test
    void delete_Throws_ResourceNotFoundException_WhenDailyNotFound() {

        when(dailyRepository.existsById(anyLong())).thenReturn(false);

        try{
            dailyService.delete(anyLong());
        } catch (ResourceNotFoundException e){
            assert true;
        }

        verify(dailyRepository, never()).deleteById(anyLong());

    }

    private DailyRequestSaveDTO createDailyRequestSaveDTO() {
        LocalDate date = LocalDate.parse("2023-05-25", DateTimeFormatter.ISO_DATE);
        return DailyRequestSaveDTO.builder()
                .dailyTaskStatusEnum(DailyTaskStatusEnum.CONCLUDED)
                .date(date)
                .personId(1L)
                .anyQuestionsMessage("testetstetstets")
                .whatWasDoneTodayMessage("ededed")
                .build();
    }

    private DailyRequestUpdateDTO createDailyRequestUpdateDTO() {
        return DailyRequestUpdateDTO.builder()
                .anyQuestionsMessage("vybsmjdmudoumudoumdodcdc")
                .dailyTaskStatusEnum(DailyTaskStatusEnum.IN_PROGRESS)
                .whatWasDoneTodayMessage("vndvsuvdsdvnusmudouc")
                .build();
    }

    private Daily createDaily() {
        LocalDate date = LocalDate.parse("2023-05-25", DateTimeFormatter.ISO_DATE);
        Person person = new Person();
        person.setId(1L);
        person.setName("vfvfv");
        return Daily.builder()
                .id(1L)
                .dailyTaskStatusEnum(DailyTaskStatusEnum.CONCLUDED)
                .whatWasDoneTodayMessage("deeded")
                .anyQuestionsMessage("ededed")
                .date(date)
                .person(person)
                .build();
    }
}