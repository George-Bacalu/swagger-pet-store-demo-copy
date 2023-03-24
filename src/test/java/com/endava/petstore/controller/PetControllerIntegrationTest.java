package com.endava.petstore.controller;

import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.ModelRequestUpdatePet;
import com.endava.petstore.model.ModelRequestUploadImage;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static com.endava.petstore.constants.Constants.IMAGE_UPLOADED;
import static com.endava.petstore.constants.Constants.PET_NOT_FOUND;
import static com.endava.petstore.constants.Constants.PET_UPDATED;
import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.PetMock.getMockedPet2;
import static com.endava.petstore.mock.PetMock.getMockedPets;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetControllerIntegrationTest {

    @Autowired
    private TestRestTemplate template;
    @Autowired
    private ObjectMapper objectMapper;

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
    void getAllPets_shouldReturnAllPets() throws Exception  {
        ResponseEntity<String> response = template.getForEntity("/pet", String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        List<Pet> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).isEqualTo(pets);
    }

    @Test
    void getPetById_withValidId_shouldReturnPetWithGivenId() {
        ResponseEntity<Pet> response = template.getForEntity("/pet/1", Pet.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(pet1);
    }

    @Test
    void getPetById_withInvalidId_shouldThrowException() {
        ResponseEntity<String> response = template.getForEntity("/pet/999", String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo("Resource not found: " + String.format(PET_NOT_FOUND, 999L));
    }

    @Test
    void savePet_shouldAddPetToList() {
        ResponseEntity<Pet> response = template.postForEntity("/pet", pet1, Pet.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(pet1);
    }

    @Test
    void updatePet_shouldModifyCurrentPet() {
        ResponseEntity<Pet> response = template.exchange("/pet", HttpMethod.PUT, null, Pet.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(pet1);
    }

    @Test
    void deletePet_shouldRemovePetFromList() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(APPLICATION_JSON);
        ResponseEntity<Pet> response = template.exchange("/pet/1", HttpMethod.DELETE, new HttpEntity<Pet>(httpHeaders), Pet.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        ResponseEntity<Pet> getResponse = template.exchange("/pet/1", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        assertNotNull(getResponse);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ResponseEntity<List<Pet>> getAllResponse = template.exchange("/pet", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        assertNotNull(getAllResponse);
        assertThat(Objects.requireNonNull(getAllResponse.getBody()).stream().anyMatch(pet -> pet.getId() == 1L)).isFalse();
        assertThat(response.getBody()).isEqualTo(pet1);
    }

    @Test
    void getPetsByStatus_shouldReturnPetsWithGivenStatuses() throws Exception {
        Status[] statuses = new Status[]{Status.AVAILABLE, Status.PENDING};
        ResponseEntity<String> response = template.getForEntity("/pet/findByStatus?status=" + statuses[0].name() + "&status=" + statuses[1].name(), String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        List<Pet> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void getPetsByTags_shouldReturnPetsWithGivenTags() throws Exception {
        List<String> tagNames = List.of("test_tag1", "test_tag2", "test_tag3", "test_tag4");
        ResponseEntity<String> response = template.getForEntity("/pet/findByTags?tags=" + tagNames.get(0) + "&tags=" + tagNames.get(1) + "&tags=" + tagNames.get(2) + "&tags=" + tagNames.get(3), String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        List<Pet> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void updatePetFormData_shouldModifyCurrentPetAccordingToForm_whenFormDataIsValid() {
        String name = "test_pet1";
        Status status = Status.AVAILABLE;
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPDATED, 1L));
        ResponseEntity<HttpResponse> response = template.postForEntity("/pet/1", new ModelRequestUpdatePet(name, status.name()), HttpResponse.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MULTIPART_FORM_DATA);
        assertThat(response.getBody()).isEqualTo(httpResponse);
    }

    @Test
    void uploadPetImage_shouldAppendToPhotoUrlsOfCurrentPet() {
        MultipartFile file = new MockMultipartFile("https:///www.test_image.jpg", "test_image.jpg", IMAGE_JPEG_VALUE, "content".getBytes());
        String additionalMetadata = "test_image";
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(IMAGE_UPLOADED, additionalMetadata, file.getOriginalFilename(), file.getSize()));
        ResponseEntity<HttpResponse> response = template.postForEntity("/pet/1/uploadImage", new ModelRequestUploadImage(additionalMetadata), HttpResponse.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MULTIPART_FORM_DATA);
        assertThat(response.getBody()).isEqualTo(httpResponse);
    }
}
