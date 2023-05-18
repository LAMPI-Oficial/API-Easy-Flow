package br.com.ifce.easyflow.controller.dto.claim;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.ifce.easyflow.model.Claim;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimRequestDTO {
    private String claim_name_user;
    private String claim_email_user;
    @NotNull
    @NotEmpty
    private String claim_descrition;

    public Claim toClaim() {
        return new Claim(claim_name_user, claim_email_user, claim_descrition);
    }

}
