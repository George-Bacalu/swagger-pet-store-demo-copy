package com.endava.petstore.repository;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.Category;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import com.endava.petstore.model.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import static com.endava.petstore.constants.Constants.PET_NOT_FOUND;

@Repository
public class PetRepositoryImpl implements PetRepository {
    
    private final Map<Long, Pet> pets = new HashMap<>();
    
    @PostConstruct
    public void initializePets() {
        Pet pet1 = Pet.builder()
              .id(1L)
              .name("test_pet1")
              .category(Category.builder().id(1L).name("test_category1").build())
              .photoUrls(List.of("https://www.test_url1.jpg", "https://www.test_url2.jpg"))
              .tags(List.of(
                    Tag.builder().id(1L).name("test_tag1").build(),
                    Tag.builder().id(2L).name("test_tag2").build()))
              .status(Status.AVAILABLE).build();
        pets.put(pet1.getId(), pet1);
        Pet pet2 = Pet.builder()
              .id(2L)
              .name("test_pet2")
              .category(Category.builder().id(2L).name("test_category2").build())
              .photoUrls(List.of("https://www.test_url3.jpg", "https://www.test_url4.jpg"))
              .tags(List.of(
                    Tag.builder().id(1L).name("test_tag3").build(),
                    Tag.builder().id(2L).name("test_tag4").build()))
              .status(Status.PENDING).build();
        pets.put(pet2.getId(), pet2);
        Pet pet3 = Pet.builder()
              .id(3L)
              .name("test_pet3")
              .category(Category.builder().id(1L).name("test_category3").build())
              .photoUrls(List.of("https://www.test_url5.jpg", "https://www.test_url6.jpg"))
              .tags(List.of(
                    Tag.builder().id(1L).name("test_tag5").build(),
                    Tag.builder().id(2L).name("test_tag6").build()))
              .status(Status.SOLD).build();
        pets.put(pet3.getId(), pet3);
    }
    
    @Override
    public List<Pet> getAllPets() {
        return new ArrayList<>(pets.values());
    }

    @Override
    public Pet getPetById(Long petId) {
        return pets.values().stream()
              .filter(pet -> Objects.equals(pet.getId(), petId)).findFirst()
              .orElseThrow(() -> new ResourceNotFoundException(String.format(PET_NOT_FOUND, petId)));
    }

    @Override
    public Pet savePet(Pet pet) {
        return pets.compute(pet.getId(), (key, value) -> pet);
    }

    @Override
    public Pet updatePet(Pet pet) {
        Pet updatedPet = getPetById(pet.getId());
        updatedPet.setName(pet.getName());
        updatedPet.setCategory(pet.getCategory());
        updatedPet.setPhotoUrls(pet.getPhotoUrls());
        updatedPet.setTags(pet.getTags());
        updatedPet.setStatus(pet.getStatus());
        return updatedPet;
    }

    @Override
    public void deletePetById(Long petId) {
        pets.remove(getPetById(petId).getId());
    }

    @Override
    public List<Pet> getPetsByStatus(Status[] statuses) {
        List<String> statusList = Arrays.stream(statuses).map(Enum::name).toList();
        return pets.values().stream().filter(pet -> statusList.stream().anyMatch(status -> pet.getStatus().name().equals(status))).toList();
    }

    @Override
    public List<Pet> getPetsByTags(List<String> tagNames) {
        return pets.values().stream().filter(pet -> pet.getTags().stream().map(Tag::getName).anyMatch(tagNames::contains)).toList();
    }
}
