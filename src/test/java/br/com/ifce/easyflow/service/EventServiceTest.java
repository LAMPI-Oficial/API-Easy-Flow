package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.event.EventRequestDTO;
import br.com.ifce.easyflow.controller.dto.event.EventResponseDTO;
import br.com.ifce.easyflow.model.Event;
import br.com.ifce.easyflow.repository.EventRepository;
import br.com.ifce.easyflow.service.exceptions.BadRequestException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.NoMoreInteractions;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @InjectMocks
    EventService eventService;

    @Mock
    EventRepository eventRepository;


    @Test
    void listAll_PageOfEventResponseDTO_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Event> eventList = List.of(createEvent());
        PageImpl<Event> eventPage = new PageImpl<>(eventList);

        when(eventRepository.findAll(pageable)).thenReturn(eventPage);

        List<EventResponseDTO> eventResponseDTO = eventService.listAll(pageable).stream().toList();

        Assertions.assertEquals(eventList.get(0).getId(), eventResponseDTO.get(0).getId());
        Assertions.assertEquals(eventList.get(0).getTime(), eventResponseDTO.get(0).getTime());
        verify(eventRepository).findAll(pageable);

    }

    @Test
    void listAll_EmptyPageOfEventResponseDTO_WhenNoEventRegistered() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Event> eventList = new ArrayList<>();
        PageImpl<Event> eventPage = new PageImpl<>(eventList);

        when(eventRepository.findAll(pageable)).thenReturn(eventPage);

        List<EventResponseDTO> eventResponseDTO = eventService.listAll(pageable).stream().toList();
        Assertions.assertTrue(eventResponseDTO.isEmpty());
        verify(eventRepository).findAll(pageable);
        verifyNoMoreInteractions(eventRepository);

    }


    @Test
    void listById_EventResponseDTO_WhenSuccessful() {
        Event event = createEvent();

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        EventResponseDTO eventResponseDTO = eventService.listById(1L);

        Assertions.assertEquals(event.getTime(), eventResponseDTO.getTime());
        Assertions.assertEquals(event.getId(), eventResponseDTO.getId());
        verify(eventRepository).findById(1L);
        verifyNoMoreInteractions(eventRepository);

    }

    @Test
    void listById_ThrowsResourceNotFoundException_WhenEventNotFound() {

        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService.listById(anyLong()));

        Assertions.assertTrue(resourceNotFoundException.getMessage().contains("Event not found with given id"));

        verifyNoMoreInteractions(eventRepository);

    }

    @Test
    void listByDate_PageOfEventResponseDTO_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Event> eventList = List.of(createEvent());
        PageImpl<Event> eventPage = new PageImpl<>(eventList);
        LocalDate date = LocalDate.parse("2023-08-25", DateTimeFormatter.ISO_DATE);

        when(eventRepository.findByDate(date, pageable)).thenReturn(eventPage);

        List<EventResponseDTO> eventResponseDTOList = eventService.listByDate("2023-08-25", pageable).stream().toList();

        Assertions.assertEquals(eventList.get(0).getDate(), eventResponseDTOList.get(0).getDate());
        Assertions.assertEquals(eventList.get(0).getId(), eventResponseDTOList.get(0).getId());

        verify(eventRepository).findByDate(date, pageable);
        verifyNoMoreInteractions(eventRepository);

    }

    @Test
    void listByDate_ThrowsBadRequestException_WhenTheDateFormatDoesNotConformToTheFormat() {
        PageRequest pageable = PageRequest.of(0, 5);
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> eventService.listByDate("20-2002-8", pageable));

        Assertions.assertTrue(badRequestException.getMessage().contains("The date format does not conform to the format: yyyy-MM-dd. "));
        verifyNoInteractions(eventRepository);

    }

    @Test
    void listByDate_PageEmpty_WhenNoEventsFoundOnThatDate() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Event> eventList = new ArrayList<>();
        PageImpl<Event> eventPage = new PageImpl<>(eventList);
        LocalDate date = LocalDate.parse("2023-08-25", DateTimeFormatter.ISO_DATE);

        when(eventRepository.findByDate(date, pageable)).thenReturn(eventPage);

        List<EventResponseDTO> eventResponseDTOList = eventService.listByDate("2023-08-25", pageable).stream().toList();

        Assertions.assertTrue(eventResponseDTOList.isEmpty());

        verify(eventRepository).findByDate(date, pageable);
        verifyNoMoreInteractions(eventRepository);

    }

    @Test
    void listByDateTime_PadeOfEventResponseDTO_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Event> eventList = List.of(createEvent());
        PageImpl<Event> eventPage = new PageImpl<>(eventList);
        LocalDate date = LocalDate.parse("2023-08-25", DateTimeFormatter.ISO_DATE);
        LocalTime time = LocalTime.parse("15:30", DateTimeFormatter.ISO_TIME);

        when(eventRepository.findByDateTime(date, time, pageable)).thenReturn(eventPage);

        List<EventResponseDTO> eventResponseDTOList = eventService.listByDateTime("2023-08-25", "15:30", pageable).stream().toList();

        Assertions.assertEquals(eventList.get(0).getId(), eventResponseDTOList.get(0).getId());
        Assertions.assertEquals(eventList.get(0).getTime(), eventResponseDTOList.get(0).getTime());
        Assertions.assertEquals(eventList.get(0).getDate(), eventResponseDTOList.get(0).getDate());
        verify(eventRepository).findByDateTime(date, time, pageable);
        verifyNoMoreInteractions(eventRepository);

    }

    @Test
    void listByDate_PageEmpty_WhenNoEventsFoundOnThatDateAndTime() {
        PageRequest pageable = PageRequest.of(0, 5);
        List<Event> eventList = new ArrayList<>();
        PageImpl<Event> eventPage = new PageImpl<>(eventList);
        LocalDate date = LocalDate.parse("2023-08-25", DateTimeFormatter.ISO_DATE);
        LocalTime time = LocalTime.parse("15:30", DateTimeFormatter.ISO_TIME);
        when(eventRepository.findByDateTime(date, time, pageable)).thenReturn(eventPage);

        List<EventResponseDTO> eventResponseDTOList = eventService.listByDateTime("2023-08-25", "15:30", pageable).stream().toList();

        Assertions.assertTrue(eventResponseDTOList.isEmpty());

        verify(eventRepository).findByDateTime(date, time, pageable);
        verifyNoMoreInteractions(eventRepository);

    }

    @Test
    void listByDate_ThrowsBadRequestException_WhenTheDateOrTimeFormatDoesNotConformToTheFormat() {
        PageRequest pageable = PageRequest.of(0, 5);

        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class,
                () -> eventService.listByDateTime("25-2255-14", "21-32", pageable));

        Assertions.assertTrue(badRequestException.getMessage().contains("The date format does not conform to the format: yyyy-MM-dd or HH:mm "));
        verifyNoInteractions(eventRepository);

    }

    @Test
    void save_ReturnEventResponseDTO_WhenSuccessful() {
        Event event = createEvent();

        when(eventRepository.save(any(Event.class))).thenReturn(event);
        EventRequestDTO eventRequestDTO = createEventRequestDTO();
        EventResponseDTO eventResponseDTO = eventService.save(eventRequestDTO);

        Assertions.assertEquals(event.getId(), eventResponseDTO.getId());
        Assertions.assertNotNull(eventResponseDTO.getId());
        Assertions.assertEquals(event.getDate(), eventResponseDTO.getDate());
        Assertions.assertEquals(event.getDescription(), eventResponseDTO.getDescription());
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void update_ReturnEventResponseDTO_WhenSuccessful() {
        Event event = createEvent();
        Event eventUpdated = createEvent();
        eventUpdated.setDescription("vfvijuyuuni");

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(eventUpdated);

        EventRequestDTO eventRequestDTO = createEventRequestDTO();
        eventRequestDTO.setDescription("vfvijuyuuni");
        EventResponseDTO eventResponseDTO = eventService.update(1L, eventRequestDTO);

        Assertions.assertEquals(eventRequestDTO.getDescription(), eventResponseDTO.getDescription());
        verifyNoMoreInteractions(eventRepository);

    }

    @Test
    void update_ThrowsResourceNotFoundException_WhenEventNotFound() {
        EventRequestDTO eventRequestDTO = createEventRequestDTO();

        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService.update(2L, eventRequestDTO));

        Assertions.assertTrue(resourceNotFoundException.getMessage().contains("Event not found with given id"));
        verifyNoMoreInteractions(eventRepository);

    }

    @Test
    void delete_WhenSuccessful() {
        Event event = createEvent();

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        doNothing().when(eventRepository).deleteById(anyLong());
        eventService.delete(1L);
        verify(eventRepository, times(1)).deleteById(1L);

    }

    @Test
    void delete_ThrowsResourceNotFoundException_WhenEventNotFoundl() {

        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> eventService.delete(2L));

        Assertions.assertTrue(resourceNotFoundException.getMessage().contains("Event not found with given id"));
        verifyNoMoreInteractions(eventRepository);

    }

    private Event createEvent(){
        LocalDate date = LocalDate.parse("2023-08-25", DateTimeFormatter.ISO_DATE);
        LocalTime time = LocalTime.parse("15:30", DateTimeFormatter.ISO_TIME);
        return Event.builder()
                .id(1L)
                .time(time)
                .date(date)
                .description("dbvdyvbydb")
                .imageUrl("vfvsfdfsfsfvf")
                .build();
    }

    private EventRequestDTO createEventRequestDTO(){
        LocalDate date = LocalDate.parse("2023-08-25", DateTimeFormatter.ISO_DATE);
        LocalTime time = LocalTime.parse("15:30", DateTimeFormatter.ISO_TIME);
        return EventRequestDTO.builder()
                .description("dbvdyvbydb")
                .imageUrl("vfvsfdfsfsfvf")
                .date(date)
                .time(time)
                .build();
    }
}