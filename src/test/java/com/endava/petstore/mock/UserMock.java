package com.endava.petstore.mock;

import com.endava.petstore.model.User;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMock {

    public static List<User> getMockedUsers() {
        return List.of(getMockedUser1(), getMockedUser2(), getMockedUser3());
    }

    public static User getMockedUser1() {
        return User.builder()
              .id(1L)
              .username("test_username1")
              .firstName("test_firstname1")
              .lastName("test_lastname1")
              .email("test_email1@email.com")
              .password("#Test_password1")
              .phone("+40700 000 001")
              .userStatus(1)
              .build();
    }

    public static User getMockedUser2() {
        return User.builder()
              .id(2L)
              .username("test_username2")
              .firstName("test_firstname2")
              .lastName("test_lastname2")
              .email("test_email2@email.com")
              .password("#Test_password2")
              .phone("+40700 000 002")
              .userStatus(2)
              .build();
    }

    public static User getMockedUser3() {
        return User.builder()
              .id(3L)
              .username("test_username3")
              .firstName("test_firstname3")
              .lastName("test_lastname3")
              .email("test_email3@email.com")
              .password("#Test_password3")
              .phone("+40700 000 003")
              .userStatus(3)
              .build();
    }
}
