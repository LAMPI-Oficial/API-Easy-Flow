package br.com.ifce.easyflow.service.announcement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.ifce.easyflow.controller.dto.announcement.AnnouncementRequestDTO;
import br.com.ifce.easyflow.controller.dto.announcement.AnnouncementUpdateDTO;
import br.com.ifce.easyflow.model.Announcement;
import br.com.ifce.easyflow.repository.AnnouncementRepository;
import br.com.ifce.easyflow.service.AnnouncementService;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AnnouncementServiceTest {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Test
    public void test() {
        Assertions.assertTrue(true);
    }

    @Test
    void search_returns_AllAnnouncement_WhenSuccessful() {
        AnnouncementRequestDTO announcementRequestDTO = createAnnouncemenRequestDTO();
        Announcement savedAnnouncement = announcementService.save(announcementRequestDTO.toAnnouncement());

        List<Announcement> announcementList = this.announcementService.search();
        Assertions.assertFalse(announcementList.isEmpty());
        Assertions.assertEquals(1, announcementList.get(0).getId());
        Assertions.assertEquals(savedAnnouncement.getTitle(), announcementList.get(0).getTitle());
        Assertions.assertEquals(savedAnnouncement.getDescrition(),
                announcementList.get(0).getDescrition());
        Assertions.assertEquals(savedAnnouncement.getImageUrl(),
                announcementList.get(0).getImageUrl());
    }

    @Test
    void searchByID_returns_AAnnouncementByTheGivenId_WhenSuccessful() {
        AnnouncementRequestDTO announcementRequestDTO = createAnnouncemenRequestDTO();
        Announcement savedAnnouncement = announcementService.save(announcementRequestDTO.toAnnouncement());
        Announcement returnedAnnouncement = this.announcementService.searchByID(savedAnnouncement.getId());

        Assertions.assertEquals(savedAnnouncement.getId(), returnedAnnouncement.getId());
        Assertions.assertEquals(savedAnnouncement.getTitle(), returnedAnnouncement.getTitle());
        Assertions.assertEquals(savedAnnouncement.getDescrition(), returnedAnnouncement.getDescrition());
        Assertions.assertEquals(savedAnnouncement.getImageUrl(), returnedAnnouncement.getImageUrl());
        Assertions.assertEquals(savedAnnouncement.getCrationDate(), returnedAnnouncement.getCrationDate());
    }

    @Test
    void searchByTitle_returns_AAnnouncementByTheGivenTitle_WhenSuccessful() {
        AnnouncementRequestDTO announcementRequestDTO = createAnnouncemenRequestDTO();
        Announcement savedAnnouncement = announcementService.save(announcementRequestDTO.toAnnouncement());
        Announcement returnedAnnouncement = this.announcementService.searchByTitle(savedAnnouncement.getTitle());

        Assertions.assertEquals(savedAnnouncement.getId(), returnedAnnouncement.getId());
        Assertions.assertEquals(savedAnnouncement.getTitle(), returnedAnnouncement.getTitle());
        Assertions.assertEquals(savedAnnouncement.getDescrition(), returnedAnnouncement.getDescrition());
        Assertions.assertEquals(savedAnnouncement.getImageUrl(), returnedAnnouncement.getImageUrl());
        Assertions.assertEquals(savedAnnouncement.getCrationDate(), returnedAnnouncement.getCrationDate());
    }

    @Test
    void save_returns_ASavedAnnouncement_WhenSuccessful() {
        AnnouncementRequestDTO announcementRequestDTO = createAnnouncemenRequestDTO();

        Announcement savedAnnouncement = announcementService.save(announcementRequestDTO.toAnnouncement());

        Assertions.assertNotNull(savedAnnouncement.getId());
        Assertions.assertEquals(announcementRequestDTO.getAnnouncement_title(), savedAnnouncement.getTitle());
        Assertions.assertEquals(announcementRequestDTO.getAnnouncement_descrition(), savedAnnouncement.getDescrition());
        Assertions.assertEquals(announcementRequestDTO.getAnnouncement_imagem_url(), savedAnnouncement.getImageUrl());
        Assertions.assertEquals(LocalDate.now(), savedAnnouncement.getCrationDate());

    }

    @Test
    void update_returns_AUpdatedAnnouncement_WhenSuccessful() {
        AnnouncementRequestDTO announcementRequestDTO = createAnnouncemenRequestDTO();
        Announcement savedAnnouncement = announcementService.save(announcementRequestDTO.toAnnouncement());
        AnnouncementUpdateDTO announcementUpdateDTO = createAnnouncementUpdateDTO();
        Announcement updatedAnnouncement = announcementService
                .update(savedAnnouncement.getId(),
                        announcementUpdateDTO.toAnnouncement(savedAnnouncement.getId()));

        Assertions.assertNotNull(updatedAnnouncement);
        Assertions.assertEquals(announcementUpdateDTO.getAnnouncement_title(), updatedAnnouncement.getTitle());
        Assertions.assertEquals(announcementUpdateDTO.getAnnouncement_descrition(),
                updatedAnnouncement.getDescrition());
        Assertions.assertEquals(announcementUpdateDTO.getAnnouncement_imagem_url(), updatedAnnouncement.getImageUrl());
        Assertions.assertEquals(LocalDate.now(), updatedAnnouncement.getCrationDate());

    }

    @Test
    void delete_returns_True_WhenSuccessful() {
        AnnouncementRequestDTO announcementRequestDTO = createAnnouncemenRequestDTO();
        Announcement savedAnnouncement = announcementService.save(announcementRequestDTO.toAnnouncement());
        boolean result = this.announcementService.delete(savedAnnouncement.getId());
        Optional<Announcement> announcement = this.announcementRepository.findById(savedAnnouncement.getId());

        Assertions.assertTrue(announcement.isEmpty());
        Assertions.assertTrue(result);
    }

    private AnnouncementRequestDTO createAnnouncemenRequestDTO() {
        AnnouncementRequestDTO announcementRequestDTO = new AnnouncementRequestDTO();
        announcementRequestDTO.setAnnouncement_title("Dia das mães");
        announcementRequestDTO.setAnnouncement_descrition("Hoje é o Dia Das Mães");
        announcementRequestDTO.setAnnouncement_imagem_url("imgmassa");
        return announcementRequestDTO;
    }

    private AnnouncementUpdateDTO createAnnouncementUpdateDTO() {
        AnnouncementUpdateDTO announcementUpdateDTO = new AnnouncementUpdateDTO();
        announcementUpdateDTO.setAnnouncement_title("Dia dos Pais");
        announcementUpdateDTO.setAnnouncement_descrition("Hoje é o Dia Dos Pais");
        announcementUpdateDTO.setAnnouncement_imagem_url("imagemmassa");
        return announcementUpdateDTO;
    }

}
