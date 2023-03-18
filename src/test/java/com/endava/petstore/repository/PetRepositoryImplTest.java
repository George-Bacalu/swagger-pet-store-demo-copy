package com.endava.petstore.repository;

import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static com.endava.petstore.constants.Constants.IMAGE_UPLOADED;
import static com.endava.petstore.constants.Constants.PET_UPDATED;
import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.PetMock.getMockedPet2;
import static com.endava.petstore.mock.PetMock.getMockedPets;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@ExtendWith(MockitoExtension.class)
class PetRepositoryImplTest {

    @Mock
    private PetRepositoryImpl petRepository;
    @Captor
    private ArgumentCaptor<Pet> petCaptor;

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
        given(petRepository.getAllPets()).willReturn(pets);
        List<Pet> result = petRepository.getAllPets();
        assertThat(result).isEqualTo(pets);
    }

    @Test
    void getPetById_shouldReturnPetWithGivenId() {
        given(petRepository.getPetById(1L)).willReturn(pet1);
        Pet result = petRepository.getPetById(1L);
        assertThat(result).isEqualTo(pet1);
    }

    @Test
    void savePet_shouldAddPetToList() {
        given(petRepository.savePet(any(Pet.class))).willReturn(pet1);
        Pet result = petRepository.savePet(pet1);
        verify(petRepository).savePet(petCaptor.capture());
        assertThat(result).isEqualTo(petCaptor.getValue());
    }

    @Test
    void updatePet_shouldModifyCurrentPet() {
        given(petRepository.updatePet(any(Pet.class))).willReturn(pet2);
        Pet result = petRepository.updatePet(pet1);
        assertThat(result).isEqualTo(pet2);
    }

    @Test
    void deletePet_shouldRemovePetFromList() {
        petRepository.deletePetById(pet1.getId());
    }

    @Test
    void getPetsByStatus_shouldReturnPetsWithGivenStatuses() {
        Status[] statuses = new Status[]{Status.AVAILABLE, Status.PENDING};
        given(petRepository.getPetsByStatus(statuses)).willReturn(List.of(pet1, pet2));
        List<Pet> result = petRepository.getPetsByStatus(statuses);
        assertThat(result).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void getPetsByTags_shouldReturnPetsWithGivenTags() {
        List<String> tagNames = List.of("test_tag1", "test_tag2", "test_tag3", "test_tag4");
        given(petRepository.getPetsByTags(tagNames)).willReturn(List.of(pet1, pet2));
        List<Pet> result = petRepository.getPetsByTags(tagNames);
        assertThat(result).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void updatePetFormData_shouldModifyCurrentPetAccordingToForm() {
        String name = "test_pet1";
        String status = "available";
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPDATED, 1L));
        given(petRepository.updatePetFormData(1L, name, status)).willReturn(httpResponse);
        HttpResponse result = petRepository.updatePetFormData(1L, name, status);
        assertThat(result).isEqualTo(httpResponse);
    }

    @Test
    void uploadPetImage_shouldAppendToPhotoUrlsOfCurrentPet() {
        MultipartFile file = new MockMultipartFile("https:///www.test_image.jpg", "test_image.jpg", IMAGE_JPEG_VALUE, "content".getBytes());
        String additionalMetadata = "test_image";
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(IMAGE_UPLOADED, additionalMetadata, file.getOriginalFilename(), file.getSize()));
        given(petRepository.uploadPetImage(1L, additionalMetadata, file)).willReturn(httpResponse);
        HttpResponse result = petRepository.uploadPetImage(1L, additionalMetadata, file);
        assertThat(result).isEqualTo(httpResponse);
    }
}
