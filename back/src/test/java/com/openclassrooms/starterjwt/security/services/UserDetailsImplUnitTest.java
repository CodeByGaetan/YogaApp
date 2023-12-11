package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class UserDetailsImplUnitTest {

    private UserDetails userDetails1;
    private UserDetails userDetails2;
    private UserDetails userDetails3;

    @BeforeEach
    public void init() {
        userDetails1 = UserDetailsImpl.builder()
            .id(1L)
            .lastName("NOM")
            .firstName("Prenom")
            .admin(true)
            .password("Youpi@456")
            .build();

        userDetails2 = UserDetailsImpl.builder()
            .id(1L)
            .lastName("NOM")
            .firstName("Prenom")
            .admin(true)
            .password("Youpi@456")
            .build();
        
        userDetails3 = UserDetailsImpl.builder()
            .id(3L)
            .lastName("NOM2")
            .firstName("Prenom2")
            .admin(false)
            .password("AZERTY@789")
            .build();
        
    }

    @Test
    public void getAdmin_userDetails_returnOk() {
        UserDetailsImpl userDetailsImpl1 = (UserDetailsImpl) userDetails1;
        assertEquals(true, userDetailsImpl1.getAdmin());
    }

    @Test
    public void equals_userDetails_returnTrue() {
        Boolean testEqual = userDetails1.equals(userDetails2);
        assertEquals(true, testEqual);
    }

    @Test
    public void equals_userDetails_returnFalse() {
        Boolean testEqual = userDetails1.equals(userDetails3);
        assertEquals(false, testEqual);
    }

    @Test
    public void equals_userDetailNull_returnFalse() {
        Boolean testEqual = userDetails1.equals(null);
        assertEquals(false, testEqual);
    }

}
