package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mappers.VetMapper;
import com.tecsup.petclinic.repositories.VetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;
    private final VetMapper vetMapper;

    @Override
    public VetDTO create(VetDTO vetDTO) {

        Vet vet = vetMapper.mapToEntity(vetDTO);

        if (vet.getActive() == null) {
            vet.setActive(Boolean.TRUE);
        }

        return vetMapper.mapToDto(vetRepository.save(vet));
    }

    @Override
    public VetDTO update(VetDTO vetDTO) {

        return vetMapper.mapToDto(vetRepository.save(vetMapper.mapToEntity(vetDTO)));
    }

    @Override
    public void delete(Integer id) throws VetNotFoundException {

        VetDTO vet = findById(id);

        vetRepository.delete(vetMapper.mapToEntity(vet));
    }

    @Override
    public VetDTO findById(Integer id) throws VetNotFoundException {

        Optional<Vet> vet = vetRepository.findById(id);

        if (vet.isEmpty()) {
            throw new VetNotFoundException("Vet record not found...!");
        }

        return vetMapper.mapToDto(vet.get());
    }

    @Override
    public List<VetDTO> findByLastName(String lastName) {

        List<Vet> vets = vetRepository.findByLastName(lastName);

        return vets.stream()
                .map(vetMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VetDTO> findActive() {

        return vetRepository.findByActiveTrue()
                .stream()
                .map(vetMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VetDTO> findAll() {

        return vetRepository.findAll()
                .stream()
                .map(vetMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
