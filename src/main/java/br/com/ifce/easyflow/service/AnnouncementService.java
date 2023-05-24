package br.com.ifce.easyflow.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifce.easyflow.model.Announcement;
import br.com.ifce.easyflow.repository.AnnouncementRepository;

@Service
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Transactional
    public Announcement save(Announcement announcement) {
        return this.announcementRepository.save(announcement);
    }

    public List<Announcement> search() {
        return this.announcementRepository.findAll();
    }

    @Transactional
    public Boolean delete(Long id) {
        Optional<Announcement> announcement = this.announcementRepository.findById(id);

        if (announcement.isPresent()) {
            this.announcementRepository.delete(announcement.get());
            return true;
        }

        return false;
    }

    public Announcement searchByID(Long id) {
        return this.announcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No announcement was found with the given id."));
    }

    @Transactional
    public Announcement update(Long id, Announcement newAnnouncement) {
        Announcement oldAnnouncement = this.searchByID(id);

        return this.save(this.fillUpdateAnnouncement(oldAnnouncement, newAnnouncement));
    }

    private Announcement fillUpdateAnnouncement(Announcement oldAnnouncement, Announcement newAnnouncement) {
        oldAnnouncement.setTitle(newAnnouncement.getTitle());
        oldAnnouncement.setDescrition(newAnnouncement.getDescrition());
        oldAnnouncement.setImageUrl(newAnnouncement.getImageUrl());

        return oldAnnouncement;
    }

    public Announcement searchByTitle(String title) {
        return this.announcementRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("No announcement were found with the given title."));
    }
}
