package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.mappers.PetTypeMapper;
import com.tecsup.petclinic.repositories.PetTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetTypeServiceImpl implements PetTypeService {

    private final PetTypeRepository petTypeRepository;
    private final PetTypeMapper petTypeMapper;

    @Override
    public PetTypeDTO create(PetTypeDTO petTypeDTO) {

        PetType petType = petTypeMapper.mapToEntity(petTypeDTO);

        if (petType.getActive() == null) {
            petType.setActive(Boolean.TRUE);
        }

        return petTypeMapper.mapToDto(petTypeRepository.save(petType));
    }

    @Override
    public PetTypeDTO update(PetTypeDTO petTypeDTO) {

        return petTypeMapper.mapToDto(petTypeRepository.save(petTypeMapper.mapToEntity(petTypeDTO)));
    }

    @Override
    public void delete(Integer id) throws PetTypeNotFoundException {

        PetTypeDTO petType = findById(id);

        petTypeRepository.delete(petTypeMapper.mapToEntity(petType));
    }

    @Override
    public PetTypeDTO findById(Integer id) throws PetTypeNotFoundException {

        Optional<PetType> petType = petTypeRepository.findById(id);

        if (petType.isEmpty()) {
            throw new PetTypeNotFoundException("Pet type record not found...!");
        }

        return petTypeMapper.mapToDto(petType.get());
    }

    @Override
    public List<PetTypeDTO> findByName(String name) {

        List<PetType> petTypes = petTypeRepository.findByName(name);

        petTypes.forEach(petType -> log.info("{}", petType));

        return petTypes.stream()
                .map(petTypeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PetTypeDTO> findActive() {

        return petTypeRepository.findByActiveTrue()
                .stream()
                .map(petTypeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PetTypeDTO> findAll() {

        return petTypeRepository.findAll()
                .stream()
                .map(petTypeMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
