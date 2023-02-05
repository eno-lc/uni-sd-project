package com.uni.sd.mappers;

import com.uni.sd.data.dto.StaffDto;
import com.uni.sd.data.entity.Staff;

public class StaffMapper {

    public static StaffDto mapToStaffDto(Staff staffEntity) {
        StaffDto staffDto = new StaffDto();
        staffDto.setId(staffEntity.getId());
        staffDto.setFirstName(staffEntity.getFirstName());
        staffDto.setLastName(staffEntity.getLastName());
        staffDto.setEmail(staffEntity.getEmail());
        staffDto.setPhone(staffEntity.getPhone());
        staffDto.setDateOfBirth(staffEntity.getDateOfBirth());
        return staffDto;
    }

    public static Staff mapToStaff(StaffDto staffDto) {
        Staff staffEntity = new Staff();
        staffEntity.setId(staffDto.getId());
        staffEntity.setFirstName(staffDto.getFirstName());
        staffEntity.setLastName(staffDto.getLastName());
        staffEntity.setEmail(staffDto.getEmail());
        staffEntity.setPhone(staffDto.getPhone());
        staffEntity.setDateOfBirth(staffDto.getDateOfBirth());
        return staffEntity;
    }

}
