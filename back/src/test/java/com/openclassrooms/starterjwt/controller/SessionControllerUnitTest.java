package com.openclassrooms.starterjwt.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

@ExtendWith(MockitoExtension.class)
public class SessionControllerUnitTest {

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionController sessionControllerUnderTest;

    private Long sessionId = 1L;
    private String name = "Pilate";
    private Long userId = 2L;

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    public void init() {
        session = new Session();
        session.setId(sessionId);
        session.setName(name);
    
        sessionDto = new SessionDto();
        sessionDto.setId(sessionId);
        sessionDto.setName(name);
    }

    @Test
    public void find_sessionById_returnOk() {
        when(sessionService.getById(sessionId)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionControllerUnderTest.findById(String.valueOf(sessionId));

        verify(sessionService).getById(sessionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void find_sessionById_returnNotFound() {
        when(sessionService.getById(sessionId)).thenReturn(null);

        ResponseEntity<?> response = sessionControllerUnderTest.findById(String.valueOf(sessionId));

        verify(sessionService).getById(sessionId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void find_sessionById_returnBadRequest() {
        when(sessionService.getById(sessionId)).thenThrow(new NumberFormatException());

        ResponseEntity<?> response = sessionControllerUnderTest.findById(String.valueOf(sessionId));

        verify(sessionService).getById(sessionId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void find_sessions_returnOk() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);

        List<SessionDto> sessionsDto = new ArrayList<>();
        sessionsDto.add(sessionDto);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionsDto);

        ResponseEntity<?> response = sessionControllerUnderTest.findAll();

        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessions);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionsDto, response.getBody());
    }

    @Test
    public void create_session_returnOk() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        

        ResponseEntity<?> response = sessionControllerUnderTest.create(sessionDto);

        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionMapper).toDto(session);
        verify(sessionService).create(session);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void update_session_returnOk() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        when(sessionService.update(sessionId, session)).thenReturn(session);
        
        ResponseEntity<?> response = sessionControllerUnderTest.update(String.valueOf(sessionId), sessionDto);

        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionMapper).toDto(session);
        verify(sessionService).update(sessionId, session);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    public void update_session_returnBadRequest() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(sessionId, session)).thenThrow(new NumberFormatException());
        
        ResponseEntity<?> response = sessionControllerUnderTest.update(String.valueOf(sessionId), sessionDto);

        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).update(sessionId, session);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void delete_session_returnOk() {
        when(sessionService.getById(sessionId)).thenReturn(session);

        ResponseEntity<?> response = sessionControllerUnderTest.save(String.valueOf(sessionId));

        verify(sessionService).getById(sessionId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void delete_session_returnNotFound() {
        when(sessionService.getById(sessionId)).thenReturn(null);

        ResponseEntity<?> response = sessionControllerUnderTest.save(String.valueOf(sessionId));

        verify(sessionService).getById(sessionId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void delete_session_returnBadRequest() {
        when(sessionService.getById(sessionId)).thenThrow(new NumberFormatException());

        ResponseEntity<?> response = sessionControllerUnderTest.save(String.valueOf(sessionId));

        verify(sessionService).getById(sessionId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void participate_session_returnOk() {
        ResponseEntity<?> response = sessionControllerUnderTest.participate(String.valueOf(sessionId), String.valueOf(userId));

        verify(sessionService).participate(sessionId, userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void participate_session_returnBadRequest() {
        doThrow(new NumberFormatException()).when(sessionService).participate(sessionId, userId);

        ResponseEntity<?> response = sessionControllerUnderTest.participate(String.valueOf(sessionId), String.valueOf(userId));

        verify(sessionService).participate(sessionId, userId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void unParticipate_session_returnOk() {
        ResponseEntity<?> response = sessionControllerUnderTest.noLongerParticipate(String.valueOf(sessionId), String.valueOf(userId));

        verify(sessionService).noLongerParticipate(sessionId, userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void unParticipate_session_returnBadRequest() {
        doThrow(new NumberFormatException()).when(sessionService).noLongerParticipate(sessionId, userId);

        ResponseEntity<?> response = sessionControllerUnderTest.noLongerParticipate(String.valueOf(sessionId), String.valueOf(userId));

        verify(sessionService).noLongerParticipate(sessionId, userId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}
