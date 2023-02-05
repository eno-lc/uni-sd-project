package com.uni.sd.data.service;

import com.uni.sd.data.dto.GradeDto;
import com.uni.sd.data.entity.Grade;
import com.uni.sd.data.repository.GradeRepository;
import com.uni.sd.mappers.GradeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeService {

    private final GradeRepository repository;

    public GradeService(GradeRepository repository) {
        this.repository = repository;
    }

    public GradeDto get(Long id) {
        Optional<Grade> entity = repository.findById(id);
        Grade grade = entity.get();
        return GradeMapper.mapToGradeDto(grade);
    }

    public GradeDto update(GradeDto entity) {
        Grade grade = GradeMapper.mapToGrade(entity);
        Grade gradeSaved = repository.save(grade);
        return GradeMapper.mapToGradeDto(gradeSaved);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<GradeDto> list(Pageable pageable) {
        Page<Grade> grades = repository.findAll(pageable);
        return grades.map(GradeMapper::mapToGradeDto);
    }

    public Page<GradeDto> list(Pageable pageable, Specification<Grade> filter) {
        Page<Grade> grades = repository.findAll(filter, pageable);
        return grades.map(GradeMapper::mapToGradeDto);
    }

    public List<GradeDto> findAllGrades(String filterText){

        List<Grade> grades = repository.findAll();

        if(filterText == null || filterText.isEmpty()){
            return grades.stream().map(GradeMapper::mapToGradeDto).collect(Collectors.toList());
        } else {
            List<Grade> gradeList = repository.search(filterText);
            return gradeList.stream()
                    .map(GradeMapper::mapToGradeDto)
                    .collect(Collectors.toList());
        }
    }
    public int count() {
        return (int) repository.count();
    }

}
