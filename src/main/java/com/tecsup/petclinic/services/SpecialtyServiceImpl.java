package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.SpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    @Override
    public SpecialtyDTO create(SpecialtyDTO specialtyDTO) {

        Specialty specialty = specialtyRepository.save(specialtyMapper.mapToEntity(specialtyDTO));

        return specialtyMapper.mapToDto(specialty);
    }

    @Override
    public SpecialtyDTO update(SpecialtyDTO specialtyDTO) {

        Specialty specialty = specialtyRepository.save(specialtyMapper.mapToEntity(specialtyDTO));

        return specialtyMapper.mapToDto(specialty);
    }

    @Override
    public void delete(Integer id) throws SpecialtyNotFoundException {

        SpecialtyDTO specialty = findById(id);

        specialtyRepository.delete(specialtyMapper.mapToEntity(specialty));
    }

    @Override
    public SpecialtyDTO findById(Integer id) throws SpecialtyNotFoundException {

        Optional<Specialty> specialty = specialtyRepository.findById(id);

        if (specialty.isEmpty()) {
            throw new SpecialtyNotFoundException("Specialty record not found...!");
        }

        return specialtyMapper.mapToDto(specialty.get());
    }

    @Override
    public List<SpecialtyDTO> findByName(String name) {

        List<Specialty> specialties = specialtyRepository.findByName(name);

        specialties.forEach(specialty -> log.info("{}", specialty));

        return specialties.stream()
                .map(specialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpecialtyDTO> findByOffice(String office) {

        List<Specialty> specialties = specialtyRepository.findByOffice(office);

        specialties.forEach(specialty -> log.info("{}", specialty));

        return specialties.stream()
                .map(specialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpecialtyDTO> findAll() {

        return specialtyRepository.findAll()
                .stream()
                .map(specialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
