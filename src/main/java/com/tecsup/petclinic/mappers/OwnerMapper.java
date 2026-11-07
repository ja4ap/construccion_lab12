package com.tecsup.petclinic.mappers;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import org.mapstruct.Mapper;

/**
 * Mapper Entity <-> DTO para Owner.
 *
 * @author Jose Paredes
 */
@Mapper(componentModel = "spring")
public interface OwnerMapper {

    Owner mapToEntity(OwnerDTO dto);

    OwnerDTO mapToDto(Owner entity);
}
