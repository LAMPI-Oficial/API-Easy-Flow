package br.com.ifce.easyflow.service.daily;

import br.com.ifce.easyflow.controller.dto.studyArea.StudyAreaUpdateDTO;
import br.com.ifce.easyflow.model.StudyArea;
import br.com.ifce.easyflow.repository.StudyAreaRepository;
import br.com.ifce.easyflow.service.daily.exceptions.ConflictException;
import br.com.ifce.easyflow.service.daily.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudyAreaService {

    private final StudyAreaRepository studyAreaRepository;

    @Autowired
    public StudyAreaService(StudyAreaRepository studyAreaRepository){
        this.studyAreaRepository = studyAreaRepository;
    }

    @Transactional
    public StudyArea save(StudyArea studyArea){
        return this.studyAreaRepository.save(studyArea);
    }

    public List<StudyArea> search(){
        return this.studyAreaRepository.findAll();
    }
    public StudyArea searchByID(Long id){
        return this.studyAreaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No study area was found with id provided."));
    }

    public Optional<StudyArea> findByStudyArea(String study_area_name){
        return this.studyAreaRepository.findByName(study_area_name);
    }

    @Transactional
    public StudyArea update(Long id, StudyAreaUpdateDTO StudyAreaUpdateDTO){
        StudyArea oldStudyArea = this.searchByID(id);

        if(!Objects.equals(oldStudyArea.getName(), StudyAreaUpdateDTO.getStudy_area_name())
                && this.existsByStudyArea(StudyAreaUpdateDTO.getStudy_area_name())){

            throw new ConflictException("There is already a study area registered with the given name.");
        }

        oldStudyArea.setName(StudyAreaUpdateDTO.getStudy_area_name());

        return this.save(oldStudyArea);
    }

    @Transactional
    public Boolean delete(Long id){
        StudyArea studyArea = this.searchByID(id);

        if(studyArea != null){
            this.studyAreaRepository.delete(studyArea);
            return true;
        }
        return false;
    }

    public StudyArea searchByName(String StudyArea_name){
        return this.studyAreaRepository
                .findByName(StudyArea_name)
                .orElseThrow(() -> new ResourceNotFoundException("No study area was found with the name provided."));
    }

    private StudyArea fillUpdateStudyArea(StudyArea oldStudyArea,StudyArea newStudyArea){
        newStudyArea.setName(oldStudyArea.getName());
        return newStudyArea;
    }

    public boolean existsByStudyArea(String StudyArea_name) {
        Optional<StudyArea> exist = this.studyAreaRepository.findByName(StudyArea_name);

        return exist.isPresent();
    }


    public boolean existsByID(Long id) {
        Optional<StudyArea> exist = this.studyAreaRepository.findById(id);

        return exist.isPresent();
    }
}
