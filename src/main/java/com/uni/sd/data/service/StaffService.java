package com.uni.sd.data.service;

import com.uni.sd.data.dto.StaffDto;
import com.uni.sd.data.entity.Staff;
import com.uni.sd.data.entity.Student;
import com.uni.sd.data.repository.StaffRepository;
import com.uni.sd.data.repository.StudentRepository;
import com.uni.sd.mappers.StaffMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService {
    private final StaffRepository repository;

    public StaffService(StaffRepository repository) {
        this.repository = repository;
    }

    public StaffDto get(Long id) {
        Optional<Staff> entity = repository.findById(id);
        Staff staff = entity.get();
        return StaffMapper.mapToStaffDto(staff);
    }

    public StaffDto update(StaffDto entity) {
        Staff staff = StaffMapper.mapToStaff(entity);
        Staff staffSaved = repository.save(staff);
        return StaffMapper.mapToStaffDto(staffSaved);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<StaffDto> list(Pageable pageable) {
        Page<Staff> staffs = repository.findAll(pageable);
        return staffs.map(StaffMapper::mapToStaffDto);
    }

    public Page<StaffDto> list(Pageable pageable, Specification<Staff> filter) {
        Page<Staff> staffs = repository.findAll(filter, pageable);
        return staffs.map(StaffMapper::mapToStaffDto);
    }


    public List<StaffDto> findAllStaff(String filterText){

        List<Staff> staffs = repository.findAll();

        if(filterText == null || filterText.isEmpty()){
            return staffs.stream().map(StaffMapper::mapToStaffDto).collect(Collectors.toList());
        } else {
            List<Staff> staffList = repository.search(filterText);
            return staffList.stream()
                    .map(StaffMapper::mapToStaffDto)
                    .collect(Collectors.toList());
        }
    }

    public int count() {
        return (int) repository.count();
    }
}
