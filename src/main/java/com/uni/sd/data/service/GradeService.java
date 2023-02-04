package com.uni.sd.data.service;

import com.uni.sd.data.entity.Grade;
import com.uni.sd.data.entity.Professor;
import com.uni.sd.data.repository.GradeRepository;
import com.uni.sd.data.repository.ProfessorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GradeService {

    private final GradeRepository repository;

    public GradeService(GradeRepository repository) {
        this.repository = repository;
    }

    public Optional<Grade> get(Long id) {
        return repository.findById(id);
    }

    public Grade update(Grade entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Grade> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Grade> list(Pageable pageable, Specification<Grade> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
