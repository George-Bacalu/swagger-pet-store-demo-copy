package com.endava.petstore.service;

import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import com.endava.petstore.repository.PetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Override
    public List<Pet> getAllPets() {
        return petRepository.getAllPets();
    }

    @Override
    public Pet getPetById(Long petId) {
        return petRepository.getPetById(petId);
    }

    @Override
    public Pet savePet(Pet pet) {
        return petRepository.savePet(pet);
    }

    @Override
    public Pet updatePet(Pet pet) {
        return petRepository.updatePet(pet);
    }

    @Override
    public void deletePetById(Long petId) {
        petRepository.deletePetById(petId);
    }

    @Override
    public List<Pet> getPetsByStatus(Status[] statuses) {
        return petRepository.getPetsByStatus(statuses);
    }
}
