package br.com.ifce.easyflow.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "announcement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "announcement_id")
    private Long id;

    @Column(name = "announcement_title")
    private String title;

    @Column(name = "announcement_descrition")
    private String descrition;

    @Column(name = "announcement_imagem_url")
    private String imageUrl;

    @Column(name = "announcement_criation_date")
    private LocalDate crationDate;

    public Announcement(String title, String descrition, String imageUrl) {
        this.title = title;
        this.descrition = descrition;
        this.imageUrl = imageUrl;
        this.crationDate = LocalDate.now();
    }
}
