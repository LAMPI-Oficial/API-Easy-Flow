package br.com.ifce.easyflow.service;

import br.com.ifce.easyflow.model.Daily;
import br.com.ifce.easyflow.repository.DailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyService {
    private final  DailyRepository dailyRepository;
    public Page<Daily> listAll(Pageable pageable){
        return this.dailyRepository.findAll(pageable);
    }

    public Page<Daily> findByUserId(Pageable pageable) {
        return this.dailyRepository.findByUserId();
    }
}
