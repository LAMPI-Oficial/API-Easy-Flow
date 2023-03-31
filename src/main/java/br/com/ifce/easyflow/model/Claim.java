package br.com.ifce.easyflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "claim")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claim_id")
    private Long id;

    @Column(name = "claim_user_name")
    private String user_name;

    @Column(name = "claim_user_email")
    private String user_email;

    @Column(name = "claim_descrition")
    private String descrition;

    @Column(name = "claim_criation_date")
    private LocalDate criationDate;

    public Claim(String claim_user_name, String claim_user_email, String claim_descrition) {
        this.user_name = claim_user_name;
        this.user_email = claim_user_email;
        this.descrition = claim_descrition;
        this.criationDate = LocalDate.now();
    }
}