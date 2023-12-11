package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplUnitTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImplUnderTest;

    private String username = "test@studio.com";

    @Test
    public void load_userByUsername_returnValidUser() {
        User user = User.builder()
            .id(1L)
            .email(username)
            .lastName("NOM")
            .firstName("Prenom")
            .password("youpi@123")
            .build();

        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findByEmail(username)).thenReturn(userOptional);

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImplUnderTest.loadUserByUsername(username);

        verify(userRepository).findByEmail(username);
        assertEquals(user.getId(), userDetails.getId());
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getLastName(), userDetails.getLastName());
        assertEquals(user.getFirstName(), userDetails.getFirstName());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    public void load_userByUsername_throwException() {
        Optional<User> userOptional = Optional.empty();

        when(userRepository.findByEmail(username)).thenReturn(userOptional);

        assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImplUnderTest.loadUserByUsername(username));
    }
}
