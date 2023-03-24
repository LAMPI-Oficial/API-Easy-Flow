package br.com.ifce.easyflow.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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

    public Optional<Announcement> searchByID(Long id) {
        return this.announcementRepository.findById(id);
    }

    @Transactional
    public Optional<Announcement> update(Announcement newAnnouncement) {
        Optional<Announcement> oldAnnouncement = this.searchByID(newAnnouncement.getId());

        return oldAnnouncement.isPresent()
                ? Optional.of(this.save(this.fillUpdateClaim(oldAnnouncement.get(), newAnnouncement)))
                : Optional.empty();
    }

    private Announcement fillUpdateClaim(Announcement oldAnnouncement, Announcement newAnnouncement) {
        oldAnnouncement.setTitle(newAnnouncement.getTitle());
        oldAnnouncement.setDescrition(newAnnouncement.getDescrition());
        oldAnnouncement.setImageUrl(newAnnouncement.getImageUrl());

        return oldAnnouncement;
    }

    public Optional<Announcement> searchByTitle(String title) {
        return this.announcementRepository.findByTitle(title);
    }
}
