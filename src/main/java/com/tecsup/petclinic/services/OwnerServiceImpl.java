package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mappers.OwnerMapper;
import com.tecsup.petclinic.repositories.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de propietarios (tabla owners).
 *
 * @author Jose Paredes
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Override
    public OwnerDTO create(OwnerDTO ownerDTO) {

        Owner newOwner = ownerRepository.save(ownerMapper.mapToEntity(ownerDTO));

        return ownerMapper.mapToDto(newOwner);
    }

    @Override
    public OwnerDTO update(OwnerDTO ownerDTO) {

        Owner newOwner = ownerRepository.save(ownerMapper.mapToEntity(ownerDTO));

        return ownerMapper.mapToDto(newOwner);
    }

    @Override
    public void delete(Integer id) throws OwnerNotFoundException {

        OwnerDTO owner = findById(id);

        ownerRepository.delete(ownerMapper.mapToEntity(owner));
    }

    @Override
    public OwnerDTO findById(Integer id) throws OwnerNotFoundException {

        Optional<Owner> owner = ownerRepository.findById(id);

        if (!owner.isPresent())
            throw new OwnerNotFoundException("Owner record not found...!");

        return ownerMapper.mapToDto(owner.get());
    }

    @Override
    public List<OwnerDTO> findByLastName(String lastName) {

        List<Owner> owners = ownerRepository.findByLastName(lastName);

        return owners.stream()
                .map(ownerMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerDTO> findByCity(String city) {

        List<Owner> owners = ownerRepository.findByCity(city);

        return owners.stream()
                .map(ownerMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerDTO> findAll() {

        return ownerRepository.findAll()
                .stream()
                .map(ownerMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
