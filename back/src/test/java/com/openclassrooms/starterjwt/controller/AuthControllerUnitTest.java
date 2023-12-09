package com.openclassrooms.starterjwt.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUnitTest {

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private JwtUtils jwtUtils;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private AuthController authControllerUnderTest;

        @Test
        public void logIn_userFoundByEmail_returnOk() {

                // Given
                Long id = 1L;
                String username = "yoga@studio.com";
                String lastName = "Studio";
                String firstName = "Yoga";
                String password = "test!1234";
                Boolean admin = true;

                String token = "tokenJwt";

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail(username);
                loginRequest.setPassword(password);

                UserDetails userDetails = UserDetailsImpl.builder()
                                .id(id)
                                .username(username)
                                .lastName(lastName)
                                .firstName(firstName)
                                .password(password)
                                .admin(admin)
                                .build();

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

                when(authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(username, password)))
                                .thenReturn(authentication);

                when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

                when(userRepository.findByEmail(username)).thenReturn(
                                Optional.of(
                                                User.builder()
                                                                .id(id)
                                                                .email(username)
                                                                .lastName(lastName)
                                                                .firstName(firstName)
                                                                .password(password)
                                                                .admin(admin)
                                                                .build()));

                // When
                ResponseEntity<?> response = authControllerUnderTest.authenticateUser(loginRequest);
                JwtResponse responseBody = (JwtResponse) response.getBody();

                // Then
                verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
                verify(jwtUtils).generateJwtToken(authentication);
                verify(userRepository).findByEmail(username);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(token, responseBody.getToken());
                assertEquals(id, responseBody.getId());
                assertEquals(username, responseBody.getUsername());
                assertEquals(lastName, responseBody.getLastName());
                assertEquals(firstName, responseBody.getFirstName());
                assertEquals(admin, responseBody.getAdmin());
        }

        @Test
        public void logIn_userNotFoundByEmail_returnOk() {

                // Given
                Long id = 1L;
                String username = "yoga@studio.com";
                String lastName = "Studio";
                String firstName = "Yoga";
                String password = "test!1234";
                Boolean admin = true;

                String token = "tokenJwt";

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail(username);
                loginRequest.setPassword(password);

                UserDetails userDetails = UserDetailsImpl.builder()
                                .id(id)
                                .username(username)
                                .lastName(lastName)
                                .firstName(firstName)
                                .password(password)
                                .admin(admin)
                                .build();

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

                when(authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(username, password)))
                                .thenReturn(authentication);

                when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

                when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

                // When
                ResponseEntity<?> response = authControllerUnderTest.authenticateUser(loginRequest);
                JwtResponse responseBody = (JwtResponse) response.getBody();

                // Then
                verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username, password));
                verify(jwtUtils).generateJwtToken(authentication);
                verify(userRepository).findByEmail(username);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(token, responseBody.getToken());
                assertEquals(id, responseBody.getId());
                assertEquals(username, responseBody.getUsername());
                assertEquals(lastName, responseBody.getLastName());
                assertEquals(firstName, responseBody.getFirstName());
                assertEquals(false, responseBody.getAdmin());
        }

        @Test
        public void register_user_returnOk() {
                // Given
                String username = "yoga@studio.com";
                String lastName = "Studio";
                String firstName = "Yoga";
                String password = "test!1234";
                String encodedPassword = "zeoiaz,ncpaé";

                SignupRequest signupRequest = new SignupRequest();
                signupRequest.setEmail(username);
                signupRequest.setLastName(lastName);
                signupRequest.setFirstName(firstName);
                signupRequest.setPassword(password);

                when(userRepository.existsByEmail(username)).thenReturn(false);
                when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

                final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

                // When
                ResponseEntity<?> response = authControllerUnderTest.registerUser(signupRequest);
                MessageResponse responseBody = (MessageResponse) response.getBody();

                verify(userRepository).save(userCaptor.capture());
                User savedUser = userCaptor.getValue();

                // Then
                verify(userRepository).existsByEmail(username);
                verify(passwordEncoder).encode(password);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals("User registered successfully!", responseBody.getMessage());
                assertEquals(username, savedUser.getEmail());
                assertEquals(lastName, savedUser.getLastName());
                assertEquals(firstName, savedUser.getFirstName());
                assertEquals(encodedPassword, savedUser.getPassword());
        }

        @Test
        public void register_user_returnBadRequest() {
                // Given
                String username = "yoga@studio.com";
                String lastName = "Studio";
                String firstName = "Yoga";
                String password = "test!1234";

                SignupRequest signupRequest = new SignupRequest();
                signupRequest.setEmail(username);
                signupRequest.setLastName(lastName);
                signupRequest.setFirstName(firstName);
                signupRequest.setPassword(password);

                when(userRepository.existsByEmail(username)).thenReturn(true);

                // When
                ResponseEntity<?> response = authControllerUnderTest.registerUser(signupRequest);
                MessageResponse responseBody = (MessageResponse) response.getBody();

                // Then
                verify(userRepository).existsByEmail(username);
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Error: Email is already taken!", responseBody.getMessage());
        }

}
