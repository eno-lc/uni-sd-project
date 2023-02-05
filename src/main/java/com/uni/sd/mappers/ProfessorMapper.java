package com.uni.sd.mappers;

import com.uni.sd.data.dto.ProfessorDto;
import com.uni.sd.data.entity.Professor;

public class ProfessorMapper {

    public static ProfessorDto mapToProfessorDto(Professor professorEntity) {
        ProfessorDto professorDto = new ProfessorDto();
        professorDto.setId(professorEntity.getId());
        professorDto.setFirstName(professorEntity.getFirstName());
        professorDto.setLastName(professorEntity.getLastName());
        professorDto.setEmail(professorEntity.getEmail());
        professorDto.setPhone(professorEntity.getPhone());
        professorDto.setDateOfBirth(professorEntity.getDateOfBirth());
        return professorDto;
    }

    public static Professor mapToProfessor(ProfessorDto professorDto) {
        Professor professorEntity = new Professor();
        professorEntity.setId(professorDto.getId());
        professorEntity.setFirstName(professorDto.getFirstName());
        professorEntity.setLastName(professorDto.getLastName());
        professorEntity.setEmail(professorDto.getEmail());
        professorEntity.setPhone(professorDto.getPhone());
        professorEntity.setDateOfBirth(professorDto.getDateOfBirth());
        return professorEntity;
    }

}
