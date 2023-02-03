package com.uni.sd.data.service;

import com.uni.sd.data.entity.Professor;
import com.uni.sd.data.entity.Student;
import com.uni.sd.data.repository.ProfessorRepository;
import com.uni.sd.data.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorService(ProfessorRepository repository) {
        this.repository = repository;
    }

    public Optional<Professor> get(Long id) {
        return repository.findById(id);
    }

    public Professor update(Professor entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Professor> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Professor> list(Pageable pageable, Specification<Professor> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
