package com.endava.petstore.service;

import com.endava.petstore.model.User;
import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long userId);

    User saveUser(User user);

    User updateUser(User user);

    void deleteUserById(Long userId);

    List<User> saveUsersArray(User[] users);

    List<User> saveUsersList(List<User> users);

    User getUserByUsername(String username);

    User updateUserByUsername(User user, String username);

    void deleteUserByUsername(String username);

    String login(String username, String password);

    String logout();
}
