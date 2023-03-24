package com.endava.petstore.repository;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static com.endava.petstore.constants.Constants.IMAGE_UPLOADED;
import static com.endava.petstore.constants.Constants.PET_NOT_FOUND;
import static com.endava.petstore.constants.Constants.PET_UPDATED;
import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.PetMock.getMockedPet2;
import static com.endava.petstore.mock.PetMock.getMockedPets;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@SpringBootTest
class PetRepositoryIntegrationTest {

    @Autowired
    private PetRepositoryImpl petRepository;

    private Pet pet1;
    private Pet pet2;
    private List<Pet> pets;

    @BeforeEach
    void setUp() {
        pet1 = getMockedPet1();
        pet2 = getMockedPet2();
        pets = getMockedPets();
    }

    @Test
    void getAllPets_shouldReturnAllPets() {
        List<Pet> result = petRepository.getAllPets();
        assertThat(result).isEqualTo(pets);
    }

    @Test
    void getPetById_withValidId_shouldReturnPetWithGivenId() {
        Pet result = petRepository.getPetById(1L);
        assertThat(result).isEqualTo(pet1);
    }

    @Test
    void getPetById_withInvalidId_shouldThrowException() {
        assertThatThrownBy(() -> petRepository.getPetById(999L))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(PET_NOT_FOUND, 999L));
    }

    @Test
    void savePet_shouldAddPetToList() {
        Pet result = petRepository.savePet(pet1);
        assertThat(result).isEqualTo(pet1);
    }

    @Test
    void updatePet_shouldModifyCurrentPet() {
        Pet result = petRepository.updatePet(pet1);
        assertThat(result).isEqualTo(pet1);
    }

    @Test
    void deletePet_shouldRemovePetFromList() {
        petRepository.deletePetById(pet1.getId());
        assertThatThrownBy(() -> petRepository.getPetById(999L))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(PET_NOT_FOUND, 999L);
    }

    @Test
    void getPetsByStatus_shouldReturnPetsWithGivenStatuses() {
        Status[] statuses = new Status[]{Status.AVAILABLE, Status.PENDING};
        List<Pet> result = petRepository.getPetsByStatus(statuses);
        assertThat(result).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void getPetsByTags_shouldReturnPetsWithGivenTags() {
        List<String> tagNames = List.of("test_tag1", "test_tag2", "test_tag3", "test_tag4");
        List<Pet> result = petRepository.getPetsByTags(tagNames);
        assertThat(result).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void updatePetFormData_shouldModifyCurrentPetAccordingToForm() {
        String name = "test_pet1";
        Status status = Status.AVAILABLE;
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPDATED, 1L));
        HttpResponse result = petRepository.updatePetFormData(1L, name, status.name());
        assertThat(result).isEqualTo(httpResponse);
    }

    @Test
    void uploadPetImage_shouldAppendToPhotoUrlsOfCurrentPet() {
        MultipartFile file = new MockMultipartFile("https:///www.test_image.jpg", "test_image.jpg", IMAGE_JPEG_VALUE, "content".getBytes());
        String additionalMetadata = "test_image";
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(IMAGE_UPLOADED, additionalMetadata, file.getOriginalFilename(), file.getSize()));
        HttpResponse result = petRepository.uploadPetImage(1L, additionalMetadata, file);
        assertThat(result).isEqualTo(httpResponse);
    }
}
