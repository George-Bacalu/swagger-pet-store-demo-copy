package com.endava.petstore.mock;

import com.endava.petstore.model.Category;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Status;
import com.endava.petstore.model.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PetMock {

    public static List<Pet> getMockedPets() {
        return List.of(getMockedPet1(), getMockedPet2(), getMockedPet3());
    }

    public static Pet getMockedPet1() {
        return Pet.builder()
              .id(1L)
              .name("test_pet1")
              .category(Category.builder().id(1L).name("test_category1").build())
              .photoUrls(List.of("https://www.test_url1.jpg", "https://www.test_url2.jpg"))
              .tags(List.of(
                    Tag.builder().id(1L).name("test_tag1").build(),
                    Tag.builder().id(2L).name("test_tag2").build()))
              .status(Status.AVAILABLE).build();
    }

    public static Pet getMockedPet2() {
        return Pet.builder()
              .id(2L)
              .name("test_pet2")
              .category(Category.builder().id(2L).name("test_category2").build())
              .photoUrls(List.of("https://www.test_url3.jpg", "https://www.test_url4.jpg"))
              .tags(List.of(
                    Tag.builder().id(1L).name("test_tag3").build(),
                    Tag.builder().id(2L).name("test_tag4").build()))
              .status(Status.PENDING).build();
    }

    public static Pet getMockedPet3() {
        return Pet.builder()
              .id(3L)
              .name("test_pet3")
              .category(Category.builder().id(1L).name("test_category3").build())
              .photoUrls(List.of("https://www.test_url5.jpg", "https://www.test_url6.jpg"))
              .tags(List.of(
                    Tag.builder().id(1L).name("test_tag5").build(),
                    Tag.builder().id(2L).name("test_tag6").build()))
              .status(Status.SOLD).build();
    }
}
