package br.com.ifce.easyflow.service.claim;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ifce.easyflow.controller.dto.claim.ClaimUpdateDTO;
import br.com.ifce.easyflow.model.Claim;
import br.com.ifce.easyflow.repository.ClaimRepository;
import br.com.ifce.easyflow.service.ClaimService;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ClaimServiceTest {


    @InjectMocks
    private ClaimService claimService;

    @Mock
    private ClaimRepository claimRepository;

    @Test
    void test(){
        Assertions.assertTrue(true);
    }

    @Test
    void save_returns_ASavedClaim_WhenSuccessful(){
        Claim claim = createClaim();
        when(claimRepository.save(any(Claim.class))).thenReturn(claim);

        Claim savedClaim = claimService.save(claim);

        Assertions.assertNotNull(savedClaim);
        Assertions.assertNotNull(savedClaim.getId());
        Assertions.assertEquals(claim.getUser_name(), savedClaim.getUser_name());
        Assertions.assertEquals(claim.getUser_email(), savedClaim.getUser_email());
        Assertions.assertEquals(claim.getDescrition(), savedClaim.getDescrition());
        Assertions.assertEquals(claim.getCriationDate(), savedClaim.getCriationDate());
        verify(claimRepository).save(claim);
        
    }

    @Test
    void search_returns_AllClaim_WhenSuccessful() {
        Claim claim = createClaim();
        List<Claim> claimList = List.of(claim);
        when(claimRepository.findAll()).thenReturn(claimList);

        List<Claim> returnedClaimList = this.claimService.search();

        Assertions.assertFalse(returnedClaimList.isEmpty());
        Assertions.assertEquals(claim.getId(), returnedClaimList.get(0).getId());
        Assertions.assertEquals(claimList.get(0).getUser_name(), returnedClaimList.get(0).getUser_name());
        Assertions.assertEquals(claimList.get(0).getUser_email(),
                returnedClaimList.get(0).getUser_email());
        Assertions.assertEquals(claimList.get(0).getDescrition(),
                returnedClaimList.get(0).getDescrition());
        verify(claimRepository).findAll();
    }

    @Test
    void delete_returns_True_WhenSuccessful() {
        Claim claim = createClaim();
        when(claimRepository.findById(anyLong())).thenReturn(Optional.of(claim));

        boolean result = this.claimService.delete(claim.getId());

        Assertions.assertTrue(result);
        verify(claimRepository).delete(claim);
        verify(claimRepository).findById(claim.getId());
    }

    @Test
    void delete_returns_False_WhenClaimNotFound() {
        when(claimRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean result = this.claimService.delete(1L);

        Assertions.assertFalse(result);
        verify(claimRepository, never()).delete(any(Claim.class));
        verify(claimRepository).findById(1L);
    }

    @Test
    void searchByID_returns_AAnnouncementByTheGivenId_WhenSuccessful() {
        Claim claim = createClaim();
        when(claimRepository.findById(claim.getId())).thenReturn(Optional.of(claim));

        Claim returnedClaim = this.claimService.searchByID(claim.getId());

        Assertions.assertNotNull(returnedClaim);
        Assertions.assertEquals(claim.getId(), returnedClaim.getId());
        Assertions.assertEquals(claim.getUser_name(), returnedClaim.getUser_name());
        Assertions.assertEquals(claim.getDescrition(), returnedClaim.getDescrition());
        Assertions.assertEquals(claim.getUser_email(), returnedClaim.getUser_email());
        Assertions.assertEquals(claim.getCriationDate(), returnedClaim.getCriationDate());
        verify(claimRepository).findById(claim.getId());
    }

    @Test
    void searchByID_Throws_ResourceNotFoundException_WhenClaimNotFound() {
        when(claimRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = Assertions
                .assertThrows(ResourceNotFoundException.class,
                        () -> claimService.searchByID(1L));


            Assertions.assertTrue(resourceNotFoundException.getMessage()
                .contains("No claim with given id was found"));

    }

    @Test
    void update_returns_AUpdatedClaim_WhenSuccessful(){
        Claim claim = createClaim();
        ClaimUpdateDTO ClaimUpdateDTO = createClaimUpdateDTO();
        when(claimRepository.save(claim)).thenReturn(claim);
        when(claimRepository.findById(anyLong())).thenReturn(Optional.of(claim));

        Claim updatedClaim = claimService.update(claim.getId(), ClaimUpdateDTO.toClaim(claim.getId()));

        Assertions.assertNotNull(updatedClaim);
        Assertions.assertEquals(claim.getId(), updatedClaim.getId());
        Assertions.assertEquals(ClaimUpdateDTO.getClaim_user_name(), updatedClaim.getUser_name());
        Assertions.assertEquals(ClaimUpdateDTO.getClaim_user_email(), updatedClaim.getUser_email());
        Assertions.assertEquals(ClaimUpdateDTO.getClaim_descrition(), updatedClaim.getDescrition());
        Assertions.assertEquals(claim.getCriationDate(), updatedClaim.getCriationDate());
        verify(claimRepository).save(claim);

    }

    private Claim createClaim() {
        Claim claim = new Claim("Joao Marcos", "joao.marcos@gmail.com", "Não tenho nada a reclamar!");
        claim.setId(1L);
        return claim;
    }

    private ClaimUpdateDTO createClaimUpdateDTO(){
        ClaimUpdateDTO claimUpdateDTO = new ClaimUpdateDTO();

        claimUpdateDTO.setClaim_user_name("Joao Victor");
        claimUpdateDTO.setClaim_user_email("joao.victor@gmail.com");
        claimUpdateDTO.setClaim_descrition("Tenho tudo a reclamar!");

        return claimUpdateDTO;
    }
}
