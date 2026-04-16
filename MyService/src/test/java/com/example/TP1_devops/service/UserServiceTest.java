package com.example.TP1_devops.service;

import com.example.TP1_devops.entity.User;
import com.example.TP1_devops.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("John Doe", "john@example.com", "New York");
        user.setId(1L);
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
        assertEquals("john@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("john@example.com", foundUser.get().getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(999L);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserByEmail("john@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    void testGetAllUsers() {
        User user2 = new User("Jane Doe", "jane@example.com", "Los Angeles");
        user2.setId(2L);
        List<User> users = Arrays.asList(user, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.getAllUsers();

        assertEquals(2, allUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        User updatedUserData = new User("John Updated", "john.updated@example.com", "Boston");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(1L, updatedUserData);

        assertNotNull(updatedUser);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean exists = userService.userExists(1L);

        assertTrue(exists);
        verify(userRepository, times(1)).existsById(1L);
    }
}
