package com.openclassrooms.starterjwt.controller;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        public void logIn_user_returnOk()
                        throws Exception {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("yoga@studio.com");
                loginRequest.setPassword("test!1234");

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        public void logIn_user_returnBadCredential()
                        throws Exception {

                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("yoga@studio.com");
                loginRequest.setPassword("wrongPassword");

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        public void register_user_returnOk()
                        throws Exception {

                SignupRequest signupRequest = new SignupRequest();
                signupRequest.setEmail(UUID.randomUUID() + "@studio.com");
                signupRequest.setLastName("newLastName");
                signupRequest.setFirstName("newFirstName");
                signupRequest.setPassword("newPassord");

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(signupRequest)))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        public void register_user_returnBadRequest()
                        throws Exception {

                SignupRequest signupRequest = new SignupRequest();
                signupRequest.setEmail("yoga@studio.com");
                signupRequest.setLastName("newLastName");
                signupRequest.setFirstName("newFirstName");
                signupRequest.setPassword("newPassord");

                mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(signupRequest)))
                                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

}