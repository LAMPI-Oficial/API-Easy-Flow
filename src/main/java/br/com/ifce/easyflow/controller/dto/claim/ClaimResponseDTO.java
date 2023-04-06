package br.com.ifce.easyflow.controller.dto.claim;

import java.time.LocalDate;

import br.com.ifce.easyflow.model.Claim;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClaimResponseDTO {

    private Long id;
    private String claim_user_name;
    private String claim_user_email;
    private String claim_descrition;
    private LocalDate claim_criation_date;

    public ClaimResponseDTO(Claim claim) {
        this.id = claim.getId();
        this.claim_user_name = claim.getUser_name();
        this.claim_user_email = claim.getUser_email();
        this.claim_descrition = claim.getDescrition();
        this.claim_criation_date = claim.getCriationDate();
    }
}
