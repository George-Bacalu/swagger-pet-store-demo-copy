package com.endava.petstore.controller;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import com.endava.petstore.service.PetService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.endava.petstore.constants.Constants.IMAGE_UPLOADED;
import static com.endava.petstore.constants.Constants.PET_NOT_FOUND;
import static com.endava.petstore.constants.Constants.PET_UPDATED;
import static com.endava.petstore.mock.PetMock.getMockedPet1;
import static com.endava.petstore.mock.PetMock.getMockedPet2;
import static com.endava.petstore.mock.PetMock.getMockedPet3;
import static com.endava.petstore.mock.PetMock.getMockedPets;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PetService petService;

    private Pet pet1;
    private Pet pet2;
    private Pet pet3;
    private List<Pet> pets;

    @BeforeEach
    void setUp() {
        pet1 = getMockedPet1();
        pet2 = getMockedPet2();
        pet3 = getMockedPet3();
        pets = getMockedPets();
    }

    @Test
    void getAllPets_shouldReturnAllPets() throws Exception {
        given(petService.getAllPets()).willReturn(pets);
        MvcResult result = mockMvc.perform(get("/pet").accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$[*].id").value(contains(pet1.getId().intValue(),pet2.getId().intValue(), pet3.getId().intValue())))
              .andExpect(jsonPath("$[*].name").value(contains(pet1.getName(), pet2.getName(), pet3.getName())))
              .andExpect(jsonPath("$[*].category.id").value(contains(pet1.getCategory().getId().intValue(), pet2.getCategory().getId().intValue(), pet3.getCategory().getId().intValue())))
              .andExpect(jsonPath("$[*].category.name").value(contains(pet1.getCategory().getName(), pet2.getCategory().getName(), pet3.getCategory().getName())))
              .andExpect(jsonPath("$[*].photoUrls[*]").value(contains(pet1.getPhotoUrls().get(0), pet1.getPhotoUrls().get(1),
                                                                      pet2.getPhotoUrls().get(0), pet2.getPhotoUrls().get(1),
                                                                      pet3.getPhotoUrls().get(0), pet3.getPhotoUrls().get(1))))
              .andExpect(jsonPath("$[*].tags[*].id").value(contains(pet1.getTags().get(0).getId().intValue(), pet1.getTags().get(1).getId().intValue(),
                                                                    pet2.getTags().get(0).getId().intValue(), pet2.getTags().get(1).getId().intValue(),
                                                                    pet3.getTags().get(0).getId().intValue(), pet3.getTags().get(1).getId().intValue())))
              .andExpect(jsonPath("$[*].tags[*].name").value(contains(pet1.getTags().get(0).getName(), pet1.getTags().get(1).getName(),
                                                                      pet2.getTags().get(0).getName(), pet2.getTags().get(1).getName(),
                                                                      pet3.getTags().get(0).getName(), pet3.getTags().get(1).getName())))
              .andExpect(jsonPath("$[*].status").value(contains(pet1.getStatus().name(), pet2.getStatus().name(), pet3.getStatus().name())))
              .andReturn();
        verify(petService).getAllPets();
        List<Pet> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(response).isEqualTo(pets);
    }

    @Test
    void getPetById_withValidId_shouldReturnPetWithGivenId() throws Exception {
        given(petService.getPetById(1L)).willReturn(pet1);
        MvcResult result = mockMvc.perform(get("/pet/{petId}", pet1.getId()).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(pet1.getId()))
              .andExpect(jsonPath("$.name").value(pet1.getName()))
              .andExpect(jsonPath("$.category").value(pet1.getCategory()))
              .andExpect(jsonPath("$.photoUrls", hasItems(pet1.getPhotoUrls().toArray())))
              .andExpect(jsonPath("$.tags[*].id", containsInAnyOrder(1, 2)))
              .andExpect(jsonPath("$.tags[*].name", containsInAnyOrder("test_tag1", "test_tag2")))
              .andExpect(jsonPath("$.status").value(pet1.getStatus().name()))
              .andReturn();
        verify(petService).getPetById(1L);
        Pet response = objectMapper.readValue(result.getResponse().getContentAsString(), Pet.class);
        assertThat(response).isEqualTo(pet1);
    }

    @Test
    void getPetById_withInvalidId_shouldThrowException() throws Exception {
        Long petId = 999L;
        given(petService.getPetById(petId)).willThrow(new ResourceNotFoundException(String.format(PET_NOT_FOUND, petId)));
        mockMvc.perform(get("/pet/{petId}", petId).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isNotFound())
              .andReturn();
        verify(petService).getPetById(petId);
    }

    @Test
    void savePet_shouldAddPetToList() throws Exception {
        given(petService.savePet(any(Pet.class))).willReturn(pet1);
        MvcResult result = mockMvc.perform(post("/pet").accept(APPLICATION_JSON_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(pet1)))
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.id").value(pet1.getId()))
              .andExpect(jsonPath("$.name").value(pet1.getName()))
              .andExpect(jsonPath("$.category").value(pet1.getCategory()))
              .andExpect(jsonPath("$.photoUrls", hasItems(pet1.getPhotoUrls().toArray())))
              .andExpect(jsonPath("$.tags[*].id", containsInAnyOrder(1, 2)))
              .andExpect(jsonPath("$.tags[*].name", containsInAnyOrder("test_tag1", "test_tag2")))
              .andExpect(jsonPath("$.status").value(pet1.getStatus().name()))
              .andReturn();
        verify(petService).savePet(pet1);
        Pet response = objectMapper.readValue(result.getResponse().getContentAsString(), Pet.class);
        assertThat(response).isEqualTo(pet1);
    }

    @Test
    void updatePet_shouldModifyCurrentPet() throws Exception {
        given(petService.updatePet(any(Pet.class))).willReturn(pet2);
        MvcResult result = mockMvc.perform(put("/pet").accept(APPLICATION_JSON_VALUE)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(pet2)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(pet2.getId()))
              .andExpect(jsonPath("$.name").value(pet2.getName()))
              .andExpect(jsonPath("$.category").value(pet2.getCategory()))
              .andExpect(jsonPath("$.photoUrls", hasItems(pet2.getPhotoUrls().toArray())))
              .andExpect(jsonPath("$.tags[*].id", containsInAnyOrder(1, 2)))
              .andExpect(jsonPath("$.tags[*].name", containsInAnyOrder("test_tag3", "test_tag4")))
              .andExpect(jsonPath("$.status").value(pet2.getStatus().name()))
              .andReturn();
        verify(petService).updatePet(pet2);
        Pet response = objectMapper.readValue(result.getResponse().getContentAsString(), Pet.class);
        assertThat(response).isEqualTo(pet2);
    }

    @Test
    void deletePet_shouldRemovePetFromList() throws Exception {
        mockMvc.perform(delete("/pet/{id}", pet1.getId()).accept(APPLICATION_JSON_VALUE))
              .andExpect(status().isNoContent())
              .andReturn();
        verify(petService).deletePetById(pet1.getId());
    }

    @Test
    void getPetsByStatus_shouldReturnPetsWithGivenStatuses() throws Exception {
        Status[] statuses = new Status[]{Status.AVAILABLE, Status.PENDING};
        given(petService.getPetsByStatus(statuses)).willReturn(List.of(pet1, pet2));
        MvcResult result = mockMvc.perform(get("/pet/findByStatus?status=" + statuses[0].name() + "&status=" + statuses[1].name()))
              .andExpect(status().isOk())
              .andReturn();
        verify(petService).getPetsByStatus(statuses);
        List<Pet> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(response).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void getPetsByTags_shouldReturnPetsWithGivenTags() throws Exception {
        List<String> tagNames = List.of("test_tag1", "test_tag2", "test_tag3", "test_tag4");
        given(petService.getPetsByTags(tagNames)).willReturn(List.of(pet1, pet2));
        MvcResult result = mockMvc.perform(get("/pet/findByTags?tags=" + tagNames.get(0) + "&tags=" + tagNames.get(1) + "&tags=" + tagNames.get(2) + "&tags=" + tagNames.get(3)))
              .andExpect(status().isOk())
              .andReturn();
        verify(petService).getPetsByTags(tagNames);
        List<Pet> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(response).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void getPetsByTags_withEmptyList_shouldThrowException() throws Exception {
        List<String> tagNames = List.of();
        mockMvc.perform(get("/pet/findByTags"))
              .andExpect(status().isBadRequest())
              .andReturn();
        verify(petService, never()).getPetsByTags(tagNames);
    }

    @Test
    void updatePetFormData_shouldModifyCurrentPetAccordingToForm_whenFormDataIsValid() throws Exception {
        String name = "test_pet1";
        Status status = Status.AVAILABLE;
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPDATED, 1L));
        given(petService.updatePetFormData(1L, name, status.name().toUpperCase())).willReturn(httpResponse);
        // The request payload must be passed as multipart form data instead of a JSON string ('objectMapper.writeValueAsString(new ModelRequestUpdatePet(name, status.name()))')
        // MockMultipartFile must be used to send multipart form data in a POST request with MockMvc. This creates a file part for the form data.
        MvcResult result = mockMvc.perform(multipart("/pet/{petId}", pet1.getId())
                        .file(new MockMultipartFile("name", name.getBytes()))
                        .file(new MockMultipartFile("status", status.name().toUpperCase().getBytes())))
                    //.contentType(MULTIPART_FORM_DATA_VALUE))
                    //.content(objectMapper.writeValueAsString(new ModelRequestUpdatePet(name, status.name()))))
              .andExpect(status().isOk())
              .andReturn();
        verify(petService).updatePetFormData(1L, name, status.name().toUpperCase());
        HttpResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), HttpResponse.class);
        assertThat(response).isEqualTo(httpResponse);
    }

    @Test
    void uploadPetImage_shouldAppendToPhotoUrlsOfCurrentPet() throws Exception {
        MockMultipartFile file = new MockMultipartFile("https:///www.test_image.jpg", "test_image.jpg", IMAGE_JPEG_VALUE, "content".getBytes());
        String additionalMetadata = "test_image";
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(IMAGE_UPLOADED, additionalMetadata, file.getOriginalFilename(), file.getSize()));
        given(petService.uploadPetImage(1L, additionalMetadata, file)).willReturn(httpResponse);
        MvcResult result = mockMvc.perform(multipart("/pet/{petId}/uploadImage", pet1.getId())
                          .file(new MockMultipartFile("additionalMetadata", additionalMetadata.getBytes()))
                          .file(file))
                //.contentType(MULTIPART_FORM_DATA_VALUE)
                //.content(objectMapper.writeValueAsString(new ModelRequestUploadImage(additionalMetadata))))
              .andExpect(status().isOk())
              .andReturn();
        verify(petService).uploadPetImage(1L, additionalMetadata, file);
        HttpResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), HttpResponse.class);
        assertThat(response).isEqualTo(httpResponse);
    }
}
