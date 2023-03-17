package com.endava.petstore.controller;

import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.ModelRequestUpdatePet;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import com.endava.petstore.service.PetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Api(value = "Pet Rest Controller", description = "Everything about your pets", tags = "pet")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pet", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class PetController {

    private final PetService petService;

    @ApiOperation(value = "Get all pets", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 404, message = "No pets found"),
          @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @ApiOperation(value = "Find pet by ID", notes = "Returns a single pet", response = Pet.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Pet not found")})
    @GetMapping("/{petId}")
    public ResponseEntity<Pet> getPetById(@ApiParam(value = "ID of pet to return", example = "1", required = true) @PathVariable Long petId) {
        return ResponseEntity.ok(petService.getPetById(petId));
    }

    @ApiOperation(value = "Add a new pet to the store", response = Pet.class)
    @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Successful operation"),
          @ApiResponse(code = 405, message = "Invalid input")})
    @PostMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<Pet> savePet(@ApiParam(value = "Pet object that needs to be added to the store", required = true) @RequestBody @Valid Pet pet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.savePet(pet));
    }

    @ApiOperation(value = "Update an existing pet", response = Pet.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Pet not found"),
          @ApiResponse(code = 405, message = "Validation exception")})
    @PutMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<Pet> updatePet(@ApiParam(value = "Pet object that needs to be added to the store", required = true) @RequestBody @Valid Pet pet) {
        return ResponseEntity.ok(petService.updatePet(pet));
    }

    @ApiOperation(value = "Deletes a pet")
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Pet not found")})
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@ApiParam(value = "Pet ID to delete", example = "1", required = true) @PathVariable Long petId) {
        petService.deletePetById(petId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Finds pets by status", notes = "Multiple status values can be provided with comma separated string", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid status value")})
    @GetMapping("/findByStatus")
    public ResponseEntity<List<Pet>> getPetsByStatus(@ApiParam(value = "Status values that need to be considered for filter", allowableValues = "available, pending, sold", allowMultiple = true, required = true) @RequestParam @Valid Status[] status) {
        return ResponseEntity.ok(petService.getPetsByStatus(status));
    }

    @ApiOperation(value = "Finds pets by tags", notes = "Multiple tags can be provided with comma separated strings. Use test_tag1, test_tag2, test_tag3 for testing", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid tag value")})
    @GetMapping("/findByTags")
    public ResponseEntity<List<Pet>> getPetsByTags(@ApiParam(value = "Tags to filter by", allowMultiple = true, required = true) @RequestParam @Valid List<String> tags) {
        if(tags.isEmpty()) {
            throw new ResourceNotFoundException("No tags were provided");
        }
        return ResponseEntity.ok(petService.getPetsByTags(tags));
    }

    @ApiOperation(value = "Updates a pet in the store with form data", response = Pet.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 405, message = "Invalid input")})
    @PostMapping(value = "/{petId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse> updatePetFormData(@ModelAttribute ModelRequestUpdatePet modelRequestUpdatePet, @ApiParam(value = "ID of pet that needs to be updated", example = "1", required = true) @PathVariable Long petId) {
        return ResponseEntity.ok(petService.updatePetFormData(petId, modelRequestUpdatePet.getName(), modelRequestUpdatePet.getStatus().name()));
    }
}
