package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {

    Specialty mapToEntity(SpecialtyDTO dto);

    SpecialtyDTO mapToDto(Specialty entity);
}
