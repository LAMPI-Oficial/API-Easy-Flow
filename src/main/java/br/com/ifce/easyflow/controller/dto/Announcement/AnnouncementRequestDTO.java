package br.com.ifce.easyflow.controller.dto.announcement;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.ifce.easyflow.model.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequestDTO {

    @NotNull
    @NotEmpty
    private String announcement_title;
    private String announcement_descrition;
    private String announcement_imagem_url;

    public Announcement toAnnouncement() {
        return new Announcement(announcement_title, announcement_descrition, announcement_imagem_url);
    }

}
