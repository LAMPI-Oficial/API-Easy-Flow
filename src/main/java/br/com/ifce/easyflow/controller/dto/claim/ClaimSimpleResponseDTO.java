package br.com.ifce.easyflow.controller.dto.claim;

import br.com.ifce.easyflow.model.Claim;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ClaimSimpleResponseDTO {

    private Long id;
    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    private LocalDate createdAt;

    public ClaimSimpleResponseDTO(Claim claim) {
        this.id = claim.getId();
        this.description = claim.getDescrition();
        this.createdAt = claim.getCriationDate();
    }
}
