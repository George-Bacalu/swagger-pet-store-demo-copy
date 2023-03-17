package com.endava.petstore.controller;

import com.endava.petstore.exception.InvalidResourceException;
import com.endava.petstore.model.Pet;
import com.endava.petstore.service.PetService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.endava.petstore.constants.Constants.INVALID_REQUEST_BODY;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping("/{petId}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long petId) {
        return ResponseEntity.ok(petService.getPetById(petId));
    }

    @PostMapping
    public ResponseEntity<Pet> savePet(@RequestBody Pet pet, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidResourceException(INVALID_REQUEST_BODY);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.savePet(pet));
    }

    @PutMapping
    public ResponseEntity<Pet> updatePet(@RequestBody Pet pet, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidResourceException(INVALID_REQUEST_BODY);
        }
        return ResponseEntity.ok(petService.updatePet(pet));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        petService.deletePetById(petId);
        return ResponseEntity.noContent().build();
    }
}
