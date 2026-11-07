package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetTypeMapper {

    PetType mapToEntity(PetTypeDTO dto);

    PetTypeDTO mapToDto(PetType entity);
}
