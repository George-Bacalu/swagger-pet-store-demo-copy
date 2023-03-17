package com.endava.petstore.repository;

import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import java.util.List;

public interface PetRepository {

    List<Pet> getAllPets();

    Pet getPetById(Long petId);

    Pet savePet(Pet pet);

    Pet updatePet(Pet pet);

    void deletePetById(Long petId);

    List<Pet> getPetsByStatus(Status[] statuses);
}
