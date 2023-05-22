package br.com.ifce.easyflow.service.announcement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.ifce.easyflow.controller.dto.announcement.AnnouncementUpdateDTO;
import br.com.ifce.easyflow.model.Announcement;
import br.com.ifce.easyflow.repository.AnnouncementRepository;
import br.com.ifce.easyflow.service.AnnouncementService;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class AnnouncementServiceTest {

    @InjectMocks
    private AnnouncementService announcementService;

    @Mock
    private AnnouncementRepository announcementRepository;


    @Test
    void search_returns_AllAnnouncement_WhenSuccessful() {
        Announcement announcement = createAnnouncement();
        List<Announcement> savedAnnouncements = List.of(announcement);
        when(announcementRepository.findAll()).thenReturn(savedAnnouncements);

        List<Announcement> announcementList = this.announcementService.search();

        Assertions.assertFalse(announcementList.isEmpty());
        Assertions.assertEquals(announcement.getId(), announcementList.get(0).getId());
        Assertions.assertEquals(savedAnnouncements.get(0).getTitle(), announcementList.get(0).getTitle());
        Assertions.assertEquals(savedAnnouncements.get(0).getDescrition(),
                announcementList.get(0).getDescrition());
        Assertions.assertEquals(savedAnnouncements.get(0).getImageUrl(),
                announcementList.get(0).getImageUrl());
        verify(announcementRepository).findAll();
    }

    @Test
    void search_returns_AEmptyList_WhenSuccessful() {
        List<Announcement> savedAnnouncements = new ArrayList<>();
        when(announcementRepository.findAll()).thenReturn(savedAnnouncements);

        List<Announcement> announcementList = this.announcementService.search();

        Assertions.assertTrue(announcementList.isEmpty());
        verify(announcementRepository).findAll();
    }

    @Test
    void searchByID_returns_AAnnouncementByTheGivenId_WhenSuccessful() {
        Announcement announcement = createAnnouncement();
        when(announcementRepository.findById(announcement.getId())).thenReturn(Optional.of(announcement));

        Announcement returnedAnnouncement = this.announcementService.searchByID(announcement.getId());

        Assertions.assertEquals(announcement.getId(), returnedAnnouncement.getId());
        Assertions.assertEquals(announcement.getTitle(), returnedAnnouncement.getTitle());
        Assertions.assertEquals(announcement.getDescrition(), returnedAnnouncement.getDescrition());
        Assertions.assertEquals(announcement.getImageUrl(), returnedAnnouncement.getImageUrl());
        Assertions.assertEquals(announcement.getCrationDate(), returnedAnnouncement.getCrationDate());
        verify(announcementRepository).findById(announcement.getId());
    }

    @Test
    void searchByID_Throws_ResourceNotFoundException_WhenAnnouncementNotFound() {
        when(announcementRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions
                .assertThrows(ResourceNotFoundException.class,
                        () -> announcementService.searchByID(1L));


            Assertions.assertTrue(resourceNotFoundException.getMessage()
                .contains("No announcement was found with the given id."));

    }

    @Test
    void searchByTitle_returns_AAnnouncementByTheGivenTitle_WhenSuccessful() {
        Announcement announcement = createAnnouncement();
        when(announcementRepository.findByTitle(announcement.getTitle())).thenReturn(Optional.of(announcement));

        Announcement returnedAnnouncement = this.announcementService.searchByTitle(announcement.getTitle());

        Assertions.assertEquals(announcement.getId(), returnedAnnouncement.getId());
        Assertions.assertEquals(announcement.getTitle(), returnedAnnouncement.getTitle());
        Assertions.assertEquals(announcement.getDescrition(), returnedAnnouncement.getDescrition());
        Assertions.assertEquals(announcement.getImageUrl(), returnedAnnouncement.getImageUrl());
        Assertions.assertEquals(announcement.getCrationDate(), returnedAnnouncement.getCrationDate());
        verify(announcementRepository).findByTitle(announcement.getTitle());
    }

    @Test
    void searchByTitle_Throws_ResourceNotFoundException_WhenAnnouncementNotFound() {
        when(announcementRepository.findByTitle(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions
                .assertThrows(ResourceNotFoundException.class,
                        () -> announcementService.searchByTitle("arrival of new equipment"));


            Assertions.assertTrue(resourceNotFoundException.getMessage()
                .contains("No announcement were found with the given title."));

    }

    @Test
    void save_returns_ASavedAnnouncement_WhenSuccessful() {
        Announcement announcement = createAnnouncement();
        when(announcementRepository.save(announcement)).thenReturn(announcement);

        Announcement savedAnnouncement = this.announcementService.save(announcement);

        Assertions.assertNotNull(savedAnnouncement.getId());
        Assertions.assertEquals(announcement.getTitle(), savedAnnouncement.getTitle());
        Assertions.assertEquals(announcement.getDescrition(), savedAnnouncement.getDescrition());
        Assertions.assertEquals(announcement.getImageUrl(), savedAnnouncement.getImageUrl());
        Assertions.assertEquals(LocalDate.now(), savedAnnouncement.getCrationDate());
        verify(announcementRepository).save(announcement);

    }

    @Test
    void update_returns_AUpdatedAnnouncement_WhenSuccessful() {
        Announcement announcement = createAnnouncement();
        when(announcementRepository.save(announcement)).thenReturn(announcement);
        when(announcementRepository.findById(anyLong())).thenReturn(Optional.of(announcement));
        AnnouncementUpdateDTO announcementUpdateDTO = createAnnouncementUpdateDTO();

        Announcement updatedAnnouncement = announcementService.update(announcement.getId(),
                announcementUpdateDTO.toAnnouncement(announcement.getId()));

        Assertions.assertNotNull(updatedAnnouncement);
        Assertions.assertEquals(announcementUpdateDTO.getAnnouncement_title(), updatedAnnouncement.getTitle());
        Assertions.assertEquals(announcementUpdateDTO.getAnnouncement_descrition(),
                updatedAnnouncement.getDescrition());
        Assertions.assertEquals(announcementUpdateDTO.getAnnouncement_imagem_url(), updatedAnnouncement.getImageUrl());
        Assertions.assertEquals(LocalDate.now(), updatedAnnouncement.getCrationDate());
        verify(announcementRepository).save(announcement);
    }

    @Test
    void delete_returns_True_WhenSuccessful() {
        Announcement announcement = createAnnouncement();
        when(announcementRepository.findById(anyLong())).thenReturn(Optional.of(announcement));

        boolean result = this.announcementService.delete(announcement.getId());

        Assertions.assertTrue(result);
        verify(announcementRepository).delete(announcement);
        verify(announcementRepository).findById(announcement.getId());
    }

    @Test
    void delete_returns_False_WhenAnnouncementNotFound() {
        when(announcementRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean result = this.announcementService.delete(1L);

        Assertions.assertFalse(result);
        verify(announcementRepository, never()).delete(any(Announcement.class));
        verify(announcementRepository).findById(1L);
    }

    private Announcement createAnnouncement() {
        Announcement announcement = new Announcement("Planejamento de novo projeto",
                "Novo projeto de uma API está sendo planejado",
                "AAAAAAAAAAAAAAAAA");
        announcement.setId(1L);
        return announcement;
    }

    private AnnouncementUpdateDTO createAnnouncementUpdateDTO() {
        AnnouncementUpdateDTO announcementUpdateDTO = new AnnouncementUpdateDTO();
        announcementUpdateDTO.setAnnouncement_title("Encarramento do projeto");
        announcementUpdateDTO.setAnnouncement_descrition("O projeto que estava sendo planejado teve que ser cancelado");
        announcementUpdateDTO.setAnnouncement_imagem_url("BBBBBBBBBBB");
        return announcementUpdateDTO;
    }

}
