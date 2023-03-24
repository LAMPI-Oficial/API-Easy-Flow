package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.controller.dto.event.EventResponseDTO;
import br.com.ifce.easyflow.model.Event;
import br.com.ifce.easyflow.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    public Page<Event> listAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }
}
