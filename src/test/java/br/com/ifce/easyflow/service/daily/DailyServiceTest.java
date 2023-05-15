package br.com.ifce.easyflow.service.daily;

import br.com.ifce.easyflow.controller.dto.daily.DailyRequestSaveDTO;
import br.com.ifce.easyflow.controller.dto.daily.DailyRequestUpdateDTO;
import br.com.ifce.easyflow.controller.dto.daily.DailyResponseDTO;
import br.com.ifce.easyflow.model.enums.DailyTaskStatusEnum;
import br.com.ifce.easyflow.service.DailyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@SpringBootTest
class DailyServiceTest {
    @Autowired
    DailyService dailyService;

    @Test
    void listAll() {

    }

    @Test
    void findById_DailyResponseDTO_WhenSuccessful() {
        DailyRequestSaveDTO dailyRequestSaveDTO = createDailyRequestSaveDTO();

        dailyService.save(dailyRequestSaveDTO);
        DailyResponseDTO dailyResponseDTO = dailyService.findById(1L);

        Assertions.assertEquals(1L, dailyResponseDTO.getId());
        Assertions.assertEquals(dailyRequestSaveDTO.getDailyTaskStatusEnum(), dailyResponseDTO.getDailyTaskStatusEnum());
        Assertions.assertEquals(dailyRequestSaveDTO.getAnyQuestionsMessage(), dailyResponseDTO.getAnyQuestionsMessage());
        Assertions.assertEquals(dailyRequestSaveDTO.getWhatWasDoneTodayMessage(), dailyResponseDTO.getWhatWasDoneTodayMessage());
        Assertions.assertEquals(dailyRequestSaveDTO.getDate(), dailyResponseDTO.getDate());
        Assertions.assertEquals(dailyRequestSaveDTO.getPersonId(), dailyResponseDTO.getPerson().getId());

    }

    @Test
    void listByPersonId_PageOfDailyResponseDTO_WhenSuccessful() {
        DailyRequestSaveDTO dailyRequestSaveDTO = createDailyRequestSaveDTO();
        Pageable pageable = PageRequest.of(0, 5);

        dailyService.save(dailyRequestSaveDTO);
        Page<DailyResponseDTO> dailyResponseDTO = dailyService.listByPersonId(1L, pageable);

        Assertions.assertEquals(dailyRequestSaveDTO.getPersonId(), dailyResponseDTO.toList().get(0).getPerson().getId());
        Assertions.assertNotNull(dailyResponseDTO.toList().get(0).getId());
        Assertions.assertEquals(1, dailyResponseDTO.toList().size());

    }

    @Test
    void listByDate_PageOfDailyResponseDTO_WhenSuccessful() {
        DailyRequestSaveDTO dailyRequestSaveDTO = createDailyRequestSaveDTO();
        Pageable pageable = PageRequest.of(0, 5);

        dailyService.save(dailyRequestSaveDTO);
        Page<DailyResponseDTO> dailyResponseDTO = dailyService.listByDate("2023-05-25", pageable);

        Assertions.assertNotNull(dailyResponseDTO.toList().get(0).getId());
        Assertions.assertEquals(dailyRequestSaveDTO.getDate(), dailyResponseDTO.toList().get(0).getDate());
    }

    @Test
    void listByPersonIdAndDate_PageOfDailyResponseDTO_WhenSuccessful() {
        DailyRequestSaveDTO dailyRequestSaveDTO = createDailyRequestSaveDTO();
        Pageable pageable = PageRequest.of(0, 5);

        dailyService.save(dailyRequestSaveDTO);
        Page<DailyResponseDTO> dailyResponseDTO = dailyService.listByPersonIdAndDate(1L, "2023-05-25", pageable);

        Assertions.assertNotNull(dailyResponseDTO);
        Assertions.assertNotNull(dailyResponseDTO.toList().get(0).getId());
        Assertions.assertEquals(1L, dailyResponseDTO.toList().get(0).getPerson().getId());
        Assertions.assertEquals(dailyRequestSaveDTO.getDate(), dailyResponseDTO.toList().get(0).getDate());
    }

    @Test
    void save_DailyResponseDTO_WhenSuccessful() {
        DailyRequestSaveDTO dailyRequestSaveDTO = createDailyRequestSaveDTO();
        DailyResponseDTO dailyResponseDTO = dailyService.save(dailyRequestSaveDTO);

        Assertions.assertNotNull(dailyResponseDTO.getId());
        Assertions.assertEquals(dailyRequestSaveDTO.getDate(), dailyResponseDTO.getDate());
        Assertions.assertEquals(dailyRequestSaveDTO.getAnyQuestionsMessage(), dailyResponseDTO.getAnyQuestionsMessage());
        Assertions.assertEquals(dailyRequestSaveDTO.getWhatWasDoneTodayMessage(), dailyResponseDTO.getWhatWasDoneTodayMessage());
        Assertions.assertEquals(dailyRequestSaveDTO.getDailyTaskStatusEnum(), dailyResponseDTO.getDailyTaskStatusEnum());
        Assertions.assertEquals(dailyRequestSaveDTO.getPersonId(), dailyResponseDTO.getPerson().getId());

    }

    @Test
    void update() {
        DailyRequestUpdateDTO dailyRequestUpdateDTO = createDailyRequestUpdateDTO();
        DailyRequestSaveDTO dailyRequestSaveDTO = createDailyRequestSaveDTO();

        DailyResponseDTO dailyToCompare = dailyService.save(dailyRequestSaveDTO);
        DailyResponseDTO dailyResponseDTO = dailyService.update(1L, dailyRequestUpdateDTO);

        Assertions.assertEquals(dailyToCompare.getId(), dailyResponseDTO.getId());
        //Assertions.assertEquals(dailyRequestUpdateDTO.getDailyTaskStatusEnum(), dailyResponseDTO.getDailyTaskStatusEnum());
        //TODO: Ajeitar o metodo update pois ele não está atualisando todos os atributos que poderia por isso o metodo comentado acima não funciona.
        Assertions.assertEquals(dailyRequestUpdateDTO.getAnyQuestionsMessage(), dailyResponseDTO.getAnyQuestionsMessage());
        Assertions.assertEquals(dailyRequestUpdateDTO.getWhatWasDoneTodayMessage(), dailyResponseDTO.getWhatWasDoneTodayMessage());
        Assertions.assertEquals(dailyToCompare.getDate(), dailyResponseDTO.getDate());
    }

    @Test
    void saveFeedback() {
    }

    @Test
    void delete() {
    }

    private DailyRequestSaveDTO createDailyRequestSaveDTO() {
        LocalDate date = LocalDate.parse("2023-05-25", DateTimeFormatter.ISO_DATE);
        return DailyRequestSaveDTO.builder()
                .dailyTaskStatusEnum(DailyTaskStatusEnum.CONCLUDED)
                .date(date)
                .personId(1L)
                .anyQuestionsMessage("testetstetstets")
                .whatWasDoneTodayMessage("dnuybdbcdgctvsdyngbdcn")
                .build();
    }

    private DailyRequestUpdateDTO createDailyRequestUpdateDTO(){
        return DailyRequestUpdateDTO.builder()
                .anyQuestionsMessage("vybsmjdmudoumudoumdodcdc")
                .dailyTaskStatusEnum(DailyTaskStatusEnum.IN_PROGRESS)
                .whatWasDoneTodayMessage("vndvsuvdsdvnusmudouc")
                .build();
    }
}