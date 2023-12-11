package com.openclassrooms.starterjwt.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userControllerUnderTest;

    private Long userId = 1L;

    private User user;
    private UserDto userDto;
    private UserDetails userDetails;

    @BeforeEach
    public void init() {
        String username = "test@studio.com";
        String lastName = "Studio";
        String firstName = "Yoga";
        String passwordEncoded = "test!1234";
        Boolean admin = false;

        user = new User();
        user.setId(userId);
        user.setEmail(username);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(passwordEncoded);
        user.setAdmin(admin);

        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setEmail(username);
        userDto.setLastName(lastName);
        userDto.setFirstName(firstName);
        userDto.setPassword(passwordEncoded);

        userDetails = UserDetailsImpl.builder()
                .id(userId)
                .username(username)
                .lastName(lastName)
                .firstName(firstName)
                .password(passwordEncoded)
                .build();
    }

    @Test
    public void find_userById_returnOk() {
        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = userControllerUnderTest.findById(String.valueOf(userId));

        verify(userService).findById(userId);
        verify(userMapper).toDto(user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    public void find_userById_returnNotFound() {
        when(userService.findById(userId)).thenReturn(null);

        ResponseEntity<?> response = userControllerUnderTest.findById(String.valueOf(userId));

        verify(userService).findById(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void find_userById_returnBadRequest() {
        when(userService.findById(userId)).thenThrow(new NumberFormatException());

        ResponseEntity<?> response = userControllerUnderTest.findById(String.valueOf(userId));

        verify(userService).findById(userId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void delete_userById_returnOk() {
        when(userService.findById(userId)).thenReturn(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        ResponseEntity<?> response = userControllerUnderTest.save(String.valueOf(userId));

        verify(userService).findById(userId);
        verify(securityContext).getAuthentication();
        verify(userService).delete(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void delete_userById_returnNotFound() {
        when(userService.findById(userId)).thenReturn(null);

        ResponseEntity<?> response = userControllerUnderTest.save(String.valueOf(userId));

        verify(userService).findById(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void delete_userById_returnUnauthorized() {

        user.setEmail("otherEmail@studio.com");

        when(userService.findById(userId)).thenReturn(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        ResponseEntity<?> response = userControllerUnderTest.save(String.valueOf(userId));

        verify(userService).findById(userId);
        verify(securityContext).getAuthentication();
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void delete_userById_returnBadRequest() {
        when(userService.findById(userId)).thenThrow(new NumberFormatException());

        ResponseEntity<?> response = userControllerUnderTest.save(String.valueOf(userId));

        verify(userService).findById(userId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}
