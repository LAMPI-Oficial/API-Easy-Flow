package br.com.ifce.easyflow.controller.dto.claim;

import br.com.ifce.easyflow.model.Claim;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimUpdateDTO {
    private String claim_user_name;
    private String claim_user_email;
    @NotNull
    @NotEmpty
    private String claim_descrition;

    public Claim toClaim(Long id) {
        Claim claim = new Claim();
        claim.setId(id);
        claim.setUser_name(this.claim_user_name);
        claim.setUser_email(claim_user_email);
        claim.setDescrition(claim_descrition);
        return claim;
    }

}
