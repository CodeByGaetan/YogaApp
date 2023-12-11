package com.openclassrooms.starterjwt.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    private String jwt;

    @BeforeEach
    public void init() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        jwt = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void find_teacherById_returnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/teacher/1")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void find_teacherById_returnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/teacher/0")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void find_teacherById_returnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/teacher/id")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void find_teachers_returnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/teacher")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void find_teachers_returnUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/teacher"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
