package com.app.orderapi.service;

import com.app.orderapi.DTO.UserDTO;
import com.app.orderapi.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDTO createUser(User user);

    Boolean verifyToken(String token);

    User updateUser(Long id,User user);

    void deleteUser(Long id);

    boolean isEmailIdUnique(String emailId);

    List<User> getAllUsers();

    User getUserById(Long id);

    UserDTO convertEntityToDTO(User user);

    User addItemsToCart(Long userId,Long itemId,int quantity);

    void removeItemFromCart(Long userId,Long itemId);

    User removeAllItemsFromCart(Long userId);

    List<User> searchUsers(String query);

}
