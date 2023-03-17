package com.endava.petstore.repository;

import com.endava.petstore.model.Pet;
import java.util.List;

public interface PetRepository {

    List<Pet> getAllPets();

    Pet getPetById(Long petId);

    Pet savePet(Pet pet);

    Pet updatePet(Pet pet);

    void deletePetById(Long petId);
}
