package com.endava.petstore.controller;

import com.endava.petstore.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.endava.petstore.constants.Constants.USERNAME_NOT_FOUND;
import static com.endava.petstore.constants.Constants.USER_NOT_FOUND;
import static com.endava.petstore.mock.UserMock.getMockedUser1;
import static com.endava.petstore.mock.UserMock.getMockedUser2;
import static com.endava.petstore.mock.UserMock.getMockedUsers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate template;
    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;
    private List<User> users;

    @BeforeEach
    void setUp() {
        user1 = getMockedUser1();
        user2 = getMockedUser2();
        users = getMockedUsers();
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() throws Exception  {
        ResponseEntity<String> response = template.getForEntity("/user", String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        List<User> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).isEqualTo(users);
    }

    @Test
    void getUserById_withValidId_shouldReturnUserWithGivenId() {
        ResponseEntity<User> response = template.getForEntity("/user/1", User.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(user1);
    }

    @Test
    void getUserById_withInvalidId_shouldThrowException() {
        ResponseEntity<String> response = template.getForEntity("/user/999", String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo("Resource not found: " + String.format(USER_NOT_FOUND, 999L));
    }

    @Test
    void saveUser_shouldAddUserToList() {
        ResponseEntity<User> response = template.postForEntity("/user", user1, User.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(user1);
    }

    @Test
    void updateUser_shouldModifyCurrentUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        ResponseEntity<User> response = template.exchange("/user", HttpMethod.PUT, new HttpEntity<>(user2, headers), User.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(user2);
    }

    @Test
    void deleteUser_shouldRemoveUserFromList() {
        System.out.println(users);
        ResponseEntity<Void> response = template.exchange("/user/1", HttpMethod.DELETE, new HttpEntity<>(null), Void.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<Void> getResponse = template.exchange("/user/1", HttpMethod.GET, null, Void.class);
        assertNotNull(getResponse);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ResponseEntity<Void> getAllResponse = template.exchange("/user", HttpMethod.GET, null, Void.class);
        assertNotNull(getAllResponse);
    }

    @Test
    void saveUsersArray_shouldAddMultipleUsersToList() throws Exception {
        User[] users = new User[]{user1, user2};
        ResponseEntity<String> response = template.postForEntity("/user/createWithArray", users, String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        List<User> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).isEqualTo(List.of(user1, user2));
    }

    @Test
    void saveUsersList_shouldAddMultipleUsersToList() throws Exception {
        List<User> users = List.of(user1, user2);
        ResponseEntity<String> response = template.postForEntity("/user/createWithList", users, String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        List<User> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).isEqualTo(users);
    }

    @Test
    void getUserByUsername_withValidUsername_shouldReturnUserWithGivenUsername() {
        ResponseEntity<User> response = template.getForEntity("/user/username/test_username1", User.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(user1);
    }

    @Test
    void getUserByUsername_withInvalidUsername_shouldThrowException() {
        String invalidUsername = "invalid_username";
        ResponseEntity<String> response = template.getForEntity("/user/username/invalid_username", String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo("Resource not found: " + String.format(USERNAME_NOT_FOUND, invalidUsername));
    }

    @Test
    void updateUserByUsername_shouldModifyCurrentUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        User resultUser = user2;
        resultUser.setId(1L);
        ResponseEntity<User> response = template.exchange("/user/username/test_username1", HttpMethod.PUT, new HttpEntity<>(user2, headers), User.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        assertThat(response.getBody()).isEqualTo(resultUser);
    }

    @Test
    void deleteUserByUsername_shouldRemoveUserFromList() {
        System.out.println(users);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        ResponseEntity<Void> response = template.exchange("/user/username/test_username2", HttpMethod.DELETE, new HttpEntity<User>(headers), Void.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<Void> getResponse = template.exchange("/user/username/test_username2", HttpMethod.GET, null, Void.class);
        assertNotNull(getResponse);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ResponseEntity<Void> getAllResponse = template.exchange("/user", HttpMethod.GET, null, Void.class);
        assertNotNull(getAllResponse);
    }

    @Test
    void login_shouldAuthenticateTheUserIntoTheSystem() throws Exception {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", "test_username1");
        body.add("password", "#Test_password1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> response = template.postForEntity("/user/login", new HttpEntity<>(body, headers), String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        Map<String, String> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).containsEntry("message", "Logged in successfully");
    }

    @Test
    void logout_shouldTerminateCurrentLoggedInUserSession() throws Exception {
        ResponseEntity<String> response = template.getForEntity("/user/logout", String.class);
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(APPLICATION_JSON);
        Map<String, String> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(result).containsEntry("message", "Logged out successfully");
    }
}
