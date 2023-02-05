package com.uni.sd.data.service;

import com.uni.sd.data.dto.ProfessorDto;
import com.uni.sd.data.entity.Grade;
import com.uni.sd.data.entity.Professor;
import com.uni.sd.data.entity.Student;
import com.uni.sd.data.repository.ProfessorRepository;
import com.uni.sd.data.repository.StudentRepository;
import com.uni.sd.mappers.ProfessorMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorService(ProfessorRepository repository) {
        this.repository = repository;
    }

    public ProfessorDto get(Long id) {
        Optional<Professor> entity = repository.findById(id);
        Professor professor = entity.get();
        return ProfessorMapper.mapToProfessorDto(professor);
    }

    public ProfessorDto update(ProfessorDto entity) {
        Professor professor = ProfessorMapper.mapToProfessor(entity);
        Professor professorSaved = repository.save(professor);
        return ProfessorMapper.mapToProfessorDto(professorSaved);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<ProfessorDto> list(Pageable pageable) {
        Page<Professor> professors = repository.findAll(pageable);
        return professors.map(ProfessorMapper::mapToProfessorDto);
    }

    public Page<ProfessorDto> list(Pageable pageable, Specification<Professor> filter) {
        Page<Professor> professors = repository.findAll(filter, pageable);
        return professors.map(ProfessorMapper::mapToProfessorDto);
    }

    public List<ProfessorDto> findAllProfessors(String filterText){

        List<Professor> professors = repository.findAll();

        if(filterText == null || filterText.isEmpty()){
            return professors.stream().map(ProfessorMapper::mapToProfessorDto).collect(Collectors.toList());
        } else {
            List<Professor> professorList = repository.search(filterText);
            return professorList.stream()
                    .map(ProfessorMapper::mapToProfessorDto)
                    .collect(Collectors.toList());
        }
    }

    public int count() {
        return (int) repository.count();
    }

}
