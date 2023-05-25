package br.com.ifce.easyflow.service.studyArea;

import br.com.ifce.easyflow.controller.dto.studyArea.StudyAreaUpdateDTO;
import br.com.ifce.easyflow.model.StudyArea;
import br.com.ifce.easyflow.repository.StudyAreaRepository;
import br.com.ifce.easyflow.service.StudyAreaService;
import br.com.ifce.easyflow.service.exceptions.ConflictException;
import br.com.ifce.easyflow.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StudyAreaServiceTest {

    @InjectMocks
    private StudyAreaService studyAreaService;

    @Mock
    private StudyAreaRepository studyAreaRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_StudyArea_WhenSuccessful() {
        StudyArea studyArea = new StudyArea();
        studyArea.setId(1L);
        studyArea.setName("Back-end");

        when(studyAreaRepository.save(any(StudyArea.class))).thenReturn(studyArea);

        StudyArea savedStudyArea = studyAreaService.save(studyArea);

        Assertions.assertEquals(studyArea.getId(), savedStudyArea.getId());
        Assertions.assertEquals(studyArea.getName(), savedStudyArea.getName());

        verify(studyAreaRepository).save(studyArea);
    }

    @Test
    void search_ReturnsListOfStudyAreas() {
        List<StudyArea> studyAreas = Arrays.asList(
                new StudyArea("Study Area 1"),
                new StudyArea("Study Area 2")
        );

        when(studyAreaRepository.findAll()).thenReturn(studyAreas);

        List<StudyArea> result = studyAreaService.search();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(studyAreas, result);

        verify(studyAreaRepository).findAll();
    }

    @Test
    void searchByID_ExistingID_ReturnsStudyArea() {
        StudyArea studyArea = createStudyArea();

        when(studyAreaRepository.findById(anyLong())).thenReturn(Optional.of(studyArea));

        StudyArea result = studyAreaService.searchByID(1L);

        Assertions.assertEquals(studyArea, result);

        verify(studyAreaRepository).findById(1L);
    }

    @Test
    void searchByID_NonExistingID_ThrowsResourceNotFoundException() {
        when(studyAreaRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            studyAreaService.searchByID(1L);
        });

        verify(studyAreaRepository).findById(1L);
    }

    @Test
    void findByStudyArea_ExistingName_ReturnsStudyArea() {
        StudyArea studyArea = createStudyArea();

        when(studyAreaRepository.findByName(anyString())).thenReturn(Optional.of(studyArea));

        Optional<StudyArea> result = studyAreaService.findByStudyArea("Back-end");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(studyArea, result.get());

        verify(studyAreaRepository).findByName("Back-end");
    }

    @Test
    void findByStudyArea_NonExistingName_ReturnsEmptyOptional() {
        when(studyAreaRepository.findByName(anyString())).thenReturn(Optional.empty());

        Optional<StudyArea> result = studyAreaService.findByStudyArea("Back-end");

        Assertions.assertFalse(result.isPresent());

        verify(studyAreaRepository).findByName("Back-end");
    }


    @Test
    void update_ExistingIDAndExistingName_ThrowsConflictException() {
        StudyArea oldStudyArea = new StudyArea("Old Study Area");
        StudyAreaUpdateDTO updateDTO = new StudyAreaUpdateDTO("Existing Study Area");

        when(studyAreaRepository.findById(anyLong())).thenReturn(Optional.of(oldStudyArea));
        when(studyAreaRepository.findByName(anyString())).thenReturn(Optional.of(new StudyArea()));

        Assertions.assertThrows(ConflictException.class, () -> {
            studyAreaService.update(1L, updateDTO);
        });

        verify(studyAreaRepository).findById(1L);
        verify(studyAreaRepository).findByName("Existing Study Area");
        verify(studyAreaRepository, never()).save(any(StudyArea.class));
    }

    @Test
    void delete_ExistingID_ReturnsTrue() {
        StudyArea studyArea = createStudyArea();

        when(studyAreaRepository.findById(anyLong())).thenReturn(Optional.of(studyArea));

        boolean result = studyAreaService.delete(1L);

        Assertions.assertTrue(result);

        verify(studyAreaRepository).findById(1L);
        verify(studyAreaRepository).delete(studyArea);
    }

    @Test
    void searchByName_ExistingName_ReturnsStudyArea() {
        StudyArea studyArea = createStudyArea();

        when(studyAreaRepository.findByName(anyString())).thenReturn(Optional.of(studyArea));

        StudyArea result = studyAreaService.searchByName("Back-end");

        Assertions.assertEquals(studyArea, result);

        verify(studyAreaRepository).findByName("Back-end");
    }

    @Test
    void searchByName_NonExistingName_ThrowsResourceNotFoundException() {
        when(studyAreaRepository.findByName(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            studyAreaService.searchByName("Back-end");
        });

        verify(studyAreaRepository).findByName("Back-end");
    }

    @Test
    void existsByStudyArea_ExistingName_ReturnsTrue() {
        when(studyAreaRepository.findByName(anyString())).thenReturn(Optional.of(new StudyArea()));

        boolean result = studyAreaService.existsByStudyArea("Back-end");

        Assertions.assertTrue(result);

        verify(studyAreaRepository).findByName("Back-end");
    }

    @Test
    void existsByStudyArea_NonExistingName_ReturnsFalse() {
        when(studyAreaRepository.findByName(anyString())).thenReturn(Optional.empty());

        boolean result = studyAreaService.existsByStudyArea("Back-end");

        Assertions.assertFalse(result);

        verify(studyAreaRepository).findByName("Back-end");
    }

    @Test
    void existsByID_ExistingID_ReturnsTrue() {
        when(studyAreaRepository.findById(anyLong())).thenReturn(Optional.of(new StudyArea()));

        boolean result = studyAreaService.existsByID(1L);

        Assertions.assertTrue(result);

        verify(studyAreaRepository).findById(1L);
    }

    @Test
    void existsByID_NonExistingID_ReturnsFalse() {
        when(studyAreaRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean result = studyAreaService.existsByID(1L);

        Assertions.assertFalse(result);

        verify(studyAreaRepository).findById(1L);
    }

    private StudyArea createStudyArea(){
        StudyArea studyArea = new StudyArea("Banck-end");
        return studyArea;
    }
}
