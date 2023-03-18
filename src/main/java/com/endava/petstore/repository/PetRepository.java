package com.endava.petstore.repository;

import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PetRepository {

    List<Pet> getAllPets();

    Pet getPetById(Long petId);

    Pet savePet(Pet pet);

    Pet updatePet(Pet pet);

    void deletePetById(Long petId);

    List<Pet> getPetsByStatus(Status[] statuses);

    List<Pet> getPetsByTags(List<String> tagNames);

    HttpResponse updatePetFormData(Long petId, String name, String status);

    HttpResponse uploadPetImage(Long petId, String additionalMetadata, MultipartFile file);
}
