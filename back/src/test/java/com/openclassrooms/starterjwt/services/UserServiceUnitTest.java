package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userServiceUnderTest;

    private Long userId = 1L;

    @Test
    public void delete_user_returnOk() {
        userServiceUnderTest.delete(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    public void find_userById_returnUser() {
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User testUser = userServiceUnderTest.findById(userId);

        verify(userRepository).findById(userId);
        assertEquals(user, testUser);
    }

    @Test
    public void find_userById_returnNull() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User testUser = userServiceUnderTest.findById(userId);

        verify(userRepository).findById(userId);
        assertNull(testUser);
    }


}