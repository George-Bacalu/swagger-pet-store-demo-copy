package com.endava.petstore.controller;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.ModelRequestUpdatePet;
import com.endava.petstore.model.ModelRequestUploadImage;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import com.endava.petstore.service.PetService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static com.endava.petstore.constants.Constants.IMAGE_UPLOADED;
import static com.endava.petstore.constants.Constants.PET_UPDATED;
import static com.endava.petstore.constants.Constants.TAGS_NOT_FOUND;
import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.PetMock.getMockedPet2;
import static com.endava.petstore.mock.PetMock.getMockedPets;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @InjectMocks
    private PetController petController;
    @Mock
    private PetService petService;

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
        given(petService.getAllPets()).willReturn(pets);
        ResponseEntity<List<Pet>> response = petController.getAllPets();
        verify(petService).getAllPets();
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(pets);
    }

    @Test
    void getPetById_shouldReturnPetWithGivenId() {
        given(petService.getPetById(1L)).willReturn(pet1);
        ResponseEntity<Pet> response = petController.getPetById(1L);
        verify(petService).getPetById(1L);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(pet1);
    }

    @Test
    void savePet_shouldAddPetToList() {
        given(petService.savePet(any(Pet.class))).willReturn(pet1);
        ResponseEntity<Pet> response = petController.savePet(pet1);
        verify(petService).savePet(pet1);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(pet1);
    }

    @Test
    void updatePet_shouldModifyCurrentPet() {
        given(petService.updatePet(any(Pet.class))).willReturn(pet2);
        ResponseEntity<Pet> response = petController.updatePet(pet1);
        verify(petService).updatePet(pet1);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(pet2);
    }

    @Test
    void deletePet_shouldRemovePetFromList() {
        ResponseEntity<Void> response = petController.deletePetById(pet1.getId());
        verify(petService).deletePetById(pet1.getId());
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void getPetsByStatus_shouldReturnPetsWithGivenStatuses() {
        Status[] statuses = new Status[]{Status.AVAILABLE, Status.PENDING};
        given(petService.getPetsByStatus(statuses)).willReturn(List.of(pet1, pet2));
        ResponseEntity<List<Pet>> response = petController.getPetsByStatus(statuses);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void getPetsByTags_shouldReturnPetsWithGivenTags() {
        List<String> tagNames = List.of("test_tag1", "test_tag2", "test_tag3", "test_tag4");
        given(petService.getPetsByTags(tagNames)).willReturn(List.of(pet1, pet2));
        ResponseEntity<List<Pet>> response = petController.getPetsByTags(tagNames);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void getPetsByTags_withEmptyList_shouldThrowException() {
        List<String> tagNames = List.of();
        assertThatThrownBy(() -> petController.getPetsByTags(tagNames))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(TAGS_NOT_FOUND);
    }

    @Test
    void updatePetFormData_shouldModifyCurrentPetAccordingToForm() {
        String name = "test_pet1";
        Status status = Status.AVAILABLE;
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPDATED, 1L));
        given(petService.updatePetFormData(1L, name, status.name())).willReturn(httpResponse);
        ResponseEntity<HttpResponse> response = petController.updatePetFormData(new ModelRequestUpdatePet(name, status), 1L);
        verify(petService).updatePetFormData(1L, name, status.name());
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(httpResponse);
    }

    @Test
    void uploadPetImage_shouldAppendToPhotoUrlsOfCurrentPet() {
        MultipartFile file = new MockMultipartFile("https:///www.test_image.jpg", "test_image.jpg", IMAGE_JPEG_VALUE, "content".getBytes());
        String additionalMetadata = "test_image";
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(IMAGE_UPLOADED, additionalMetadata, file.getOriginalFilename(), file.getSize()));
        given(petService.uploadPetImage(1L, additionalMetadata, file)).willReturn(httpResponse);
        ResponseEntity<HttpResponse> response = petController.uploadPetImage(new ModelRequestUploadImage(additionalMetadata), 1L, file);
        verify(petService).uploadPetImage(1L, additionalMetadata, file);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(httpResponse);
    }
}
