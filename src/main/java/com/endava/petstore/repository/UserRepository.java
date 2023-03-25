package com.endava.petstore.repository;

import com.endava.petstore.model.User;
import java.util.List;

public interface UserRepository {

    List<User> getAllUsers();

    User getUserById(Long userId);

    User saveUser(User user);

    User updateUser(User user);

    void deleteUserById(Long userId);
}
