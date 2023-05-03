package br.com.ifce.easyflow.controller;

import br.com.ifce.easyflow.controller.dto.announcement.AnnouncementRequestDTO;
import br.com.ifce.easyflow.controller.dto.announcement.AnnouncementResponseDTO;
import br.com.ifce.easyflow.controller.dto.announcement.AnnouncementUpdateDTO;
import br.com.ifce.easyflow.model.Announcement;
import br.com.ifce.easyflow.service.AnnouncementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @Autowired
    private AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @ApiOperation(value = "Returns a list of announcement", tags = {"Announcement"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),

    })

    @GetMapping
    public List<AnnouncementResponseDTO> search() {
        return this.announcementService
                .search()
                .stream()
                .map(AnnouncementResponseDTO::new)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Save a Announcement", tags = {"Announcement"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AnnouncementRequestDTO announcementRequestDTO,
                                       UriComponentsBuilder uriBuilder) {

        Announcement announcement = announcementRequestDTO.toAnnouncement();
        this.announcementService.save(announcement);

        URI uri = uriBuilder.path("/announcements/{id}").buildAndExpand(announcement.getId()).toUri();
        return ResponseEntity.created(uri).body(new AnnouncementResponseDTO(announcement));
    }

    @ApiOperation(value = "Delete a Announcement by id", tags = {"Announcement"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Announcement not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        boolean removed = this.announcementService.delete(id);

        return removed
                ? ResponseEntity.status(HttpStatus.OK).body(
                "Announcement was deleted")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                "Announcement Not Found");
    }

    @ApiOperation(value = "Returns a Announcement by id", tags = {"Announcement"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Announcement not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> searchById(@PathVariable Long id) {
        Announcement announcement = this.announcementService.searchByID(id);

        return ResponseEntity.ok(new AnnouncementResponseDTO(announcement));
    }

    @ApiOperation(value = "Update a Announcement by id", tags = {"Announcement"})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Saved reservation"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Announcement not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id,
                                         @RequestBody @Valid AnnouncementUpdateDTO AnnouncementUpdateDTO) {

        Announcement announcement = this.announcementService.update(id, AnnouncementUpdateDTO.toAnnouncement(id));

        return ResponseEntity.ok(new AnnouncementResponseDTO(announcement));

    }

    @ApiOperation(value = "Returns a Announcement by title", tags = {"Announcement"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 403, message = "Permission denied to access this resource"),
            @ApiResponse(code = 404, message = "Announcement not found in database"),
            @ApiResponse(code = 500, message = "Internal exception"),
    })
    @RequestMapping(value = "/search_by_title", method = RequestMethod.GET)
    public ResponseEntity<Object> searchByName(@RequestParam String title) {
        Announcement announcement = this.announcementService.searchByTitle(title);

        return ResponseEntity.ok(new AnnouncementResponseDTO(announcement));
    }

}
