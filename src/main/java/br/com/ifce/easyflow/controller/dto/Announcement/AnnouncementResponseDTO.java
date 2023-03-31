package br.com.ifce.easyflow.controller.dto.announcement;

import java.time.LocalDate;

import br.com.ifce.easyflow.model.Announcement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnnouncementResponseDTO {
    private Long announcement_id;
    private String announcement_title;
    private String announcement_descrition;
    private String announcement_imagem_url;
    private LocalDate announcement_criation_date;

    public AnnouncementResponseDTO(Announcement announcement) {
        this.announcement_id = announcement.getId();
        this.announcement_title = announcement.getTitle();
        this.announcement_descrition = announcement.getDescrition();
        this.announcement_imagem_url = announcement.getImageUrl();
        this.announcement_criation_date = announcement.getCrationDate();
    }

}
