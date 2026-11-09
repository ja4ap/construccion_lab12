package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.SpecialtyMapper;
import com.tecsup.petclinic.mappers.VetMapper;
import com.tecsup.petclinic.mappers.VetSpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import com.tecsup.petclinic.repositories.VetRepository;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

    private final VetSpecialtyRepository vetSpecialtyRepository;
    private final VetRepository vetRepository;
    private final SpecialtyRepository specialtyRepository;
    private final VetSpecialtyMapper vetSpecialtyMapper;
    private final VetMapper vetMapper;
    private final SpecialtyMapper specialtyMapper;

    @Override
    public VetSpecialtyDTO assign(VetSpecialtyDTO vetSpecialtyDTO)
            throws VetNotFoundException, SpecialtyNotFoundException {

        if (!vetRepository.existsById(vetSpecialtyDTO.getVetId())) {
            throw new VetNotFoundException("Vet record not found...!");
        }

        if (!specialtyRepository.existsById(vetSpecialtyDTO.getSpecialtyId())) {
            throw new SpecialtyNotFoundException("Specialty record not found...!");
        }

        VetSpecialty vetSpecialty = vetSpecialtyRepository.save(vetSpecialtyMapper.mapToEntity(vetSpecialtyDTO));

        return vetSpecialtyMapper.mapToDto(vetSpecialty);
    }

    @Override
    public VetSpecialtyDTO update(VetSpecialtyDTO vetSpecialtyDTO)
            throws VetSpecialtyNotFoundException {

        findById(vetSpecialtyDTO.getVetId(), vetSpecialtyDTO.getSpecialtyId());

        VetSpecialty vetSpecialty = vetSpecialtyRepository.save(vetSpecialtyMapper.mapToEntity(vetSpecialtyDTO));

        return vetSpecialtyMapper.mapToDto(vetSpecialty);
    }

    @Override
    public void remove(Integer vetId, Integer specialtyId)
            throws VetSpecialtyNotFoundException {

        if (!vetSpecialtyRepository.existsByVetIdAndSpecialtyId(vetId, specialtyId)) {
            throw new VetSpecialtyNotFoundException("Vet-Specialty record not found...!");
        }

        vetSpecialtyRepository.deleteById(new VetSpecialtyId(vetId, specialtyId));
    }

    @Override
    public VetSpecialtyDTO findById(Integer vetId, Integer specialtyId)
            throws VetSpecialtyNotFoundException {

        Optional<VetSpecialty> vetSpecialty =
                vetSpecialtyRepository.findByVetIdAndSpecialtyId(vetId, specialtyId);

        if (vetSpecialty.isEmpty()) {
            throw new VetSpecialtyNotFoundException("Vet-Specialty record not found...!");
        }

        return vetSpecialtyMapper.mapToDto(vetSpecialty.get());
    }

    @Override
    public List<SpecialtyDTO> findSpecialtiesByVetId(Integer vetId)
            throws VetNotFoundException {

        if (!vetRepository.existsById(vetId)) {
            throw new VetNotFoundException("Vet record not found...!");
        }

        List<Integer> specialtyIds = vetSpecialtyRepository.findByVetId(vetId)
                .stream()
                .map(VetSpecialty::getSpecialtyId)
                .collect(Collectors.toList());

        List<SpecialtyDTO> specialties = specialtyRepository.findAllById(specialtyIds)
                .stream()
                .map(specialtyMapper::mapToDto)
                .collect(Collectors.toList());

        return specialties;
    }

    @Override
    public List<VetDTO> findVetsBySpecialtyId(Integer specialtyId)
            throws SpecialtyNotFoundException {

        if (!specialtyRepository.existsById(specialtyId)) {
            throw new SpecialtyNotFoundException("Specialty record not found...!");
        }

        List<Integer> vetIds = vetSpecialtyRepository.findBySpecialtyId(specialtyId)
                .stream()
                .map(VetSpecialty::getVetId)
                .collect(Collectors.toList());

        List<VetDTO> vets = vetRepository.findAllById(vetIds)
                .stream()
                .map(vetMapper::mapToDto)
                .collect(Collectors.toList());

        return vets;
    }

    @Override
    public List<VetSpecialtyDTO> findAll() {

        return vetSpecialtyRepository.findAll()
                .stream()
                .map(vetSpecialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
