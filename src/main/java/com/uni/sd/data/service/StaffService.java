package com.uni.sd.data.service;

import com.uni.sd.data.entity.Staff;
import com.uni.sd.data.entity.Student;
import com.uni.sd.data.repository.StaffRepository;
import com.uni.sd.data.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffService {
    private final StaffRepository repository;

    public StaffService(StaffRepository repository) {
        this.repository = repository;
    }

    public Optional<Staff> get(Long id) {
        return repository.findById(id);
    }

    public Staff update(Staff entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Staff> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Staff> list(Pageable pageable, Specification<Staff> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
