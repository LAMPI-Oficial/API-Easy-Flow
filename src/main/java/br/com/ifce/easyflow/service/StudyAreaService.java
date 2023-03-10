package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.repository.StudyAreaRepository;
import br.com.ifce.easyflow.model.StudyArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
    public Optional<StudyArea> searchByID(Long id){
        return this.studyAreaRepository.findById(id);
    }

    public Optional<StudyArea> findByStudyArea(String study_area_name){
        return this.studyAreaRepository.findByName(study_area_name);
    }

    @Transactional
    public Optional<StudyArea> update(StudyArea newStudyArea){
        Optional<StudyArea> oldStudyArea = this.searchByID(newStudyArea.getId());

        return oldStudyArea.isPresent()
                ? Optional.of(this.save(this.fillUpdateStudyArea(oldStudyArea.get(),newStudyArea)))
                : Optional.empty();
    }

    @Transactional
    public Boolean delete(Long id){
        Optional<StudyArea> StudyArea = this.searchByID(id);

        if(StudyArea.isPresent()){
            this.studyAreaRepository.delete(StudyArea.get());
            return true;
        }

        return false;
    }

    public Optional<StudyArea> searchByName(String StudyArea_name){
        return this.studyAreaRepository.findByName(StudyArea_name);
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
