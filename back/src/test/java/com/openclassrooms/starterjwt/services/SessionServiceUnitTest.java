package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionServiceUnderTest;

    private Session session;
    private Long sessionId = 1L;
    private Long userId = 2L;
    final ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);

    @BeforeEach
    public void init() {

        List<User> userList =  new ArrayList<>();

        session = new Session();
        session.setId(sessionId);
        session.setName("Yoga");
        session.setUsers(userList);

        sessionCaptor.getAllValues().clear();
    }

    @Test
    public void create_session_returnSession() {
        when(sessionRepository.save(session)).thenReturn(session);

        sessionServiceUnderTest.create(session);

        verify(sessionRepository).save(session);
    }

    @Test
    public void delete_session_returnNothing() {
        sessionServiceUnderTest.delete(sessionId);

        verify(sessionRepository).deleteById(sessionId);
    }

    @Test
    public void find_sessions_returnSessions() {

        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        when(sessionRepository.findAll()).thenReturn(sessionList);

        sessionServiceUnderTest.findAll();

        verify(sessionRepository).findAll();
    }

    @Test
    public void find_sessionById_returnSession() {
        Optional<Session> sessionOptional = Optional.of(session);

        when(sessionRepository.findById(sessionId)).thenReturn(sessionOptional);

        sessionServiceUnderTest.getById(sessionId);

        verify(sessionRepository).findById(sessionId);
    }

    @Test
    public void update_session_returnSession() {
        Session updatedSession = new Session();
        updatedSession.setId(2L);
        updatedSession.setName("Meditation");

        sessionServiceUnderTest.update(sessionId, updatedSession);

        verify(sessionRepository).save(sessionCaptor.capture());
        assertEquals(sessionId, sessionCaptor.getValue().getId());
        assertEquals(updatedSession.getName(), sessionCaptor.getValue().getName());
    }

    @Test
    public void participate_session_returnOk() {
        User user = new User();
        user.setId(userId);
        user.setEmail("test@studio.com");

        Optional<User> userOptional = Optional.of(user);
        Optional<Session> sessionOptionnal = Optional.of(session);

        when(sessionRepository.findById(sessionId)).thenReturn(sessionOptionnal);
        when(userRepository.findById(userId)).thenReturn(userOptional);

        sessionServiceUnderTest.participate(sessionId, userId);

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository).save(sessionCaptor.capture());
        assertEquals(sessionCaptor.getValue().getUsers().get(0), user);
    }

    @Test
    public void participate_session_returnNotFound() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionServiceUnderTest.participate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void participate_session_returnBadRequest() {
        User user = new User();
        user.setId(userId);
        user.setEmail("test@studio.com");

        session.getUsers().add(user);

        Optional<User> userOptional = Optional.of(user);
        Optional<Session> sessionOptionnal = Optional.of(session);

        when(sessionRepository.findById(sessionId)).thenReturn(sessionOptionnal);
        when(userRepository.findById(userId)).thenReturn(userOptional);


        assertThrows(BadRequestException.class, () -> sessionServiceUnderTest.participate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void unParticipate_session_returnOk() {
        User user = new User();
        user.setId(userId);
        user.setEmail("test@studio.com");

        session.getUsers().add(user);

        Optional<Session> sessionOptionnal = Optional.of(session);

        when(sessionRepository.findById(sessionId)).thenReturn(sessionOptionnal);

        sessionServiceUnderTest.noLongerParticipate(sessionId, userId);

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).save(sessionCaptor.capture());
        assertEquals(sessionCaptor.getValue().getUsers().isEmpty(), true);
    }

    @Test
    public void unParticipate_session_returnNotFound() {
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionServiceUnderTest.noLongerParticipate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    public void unParticipate_session_returnBadRequest() {
        Optional<Session> sessionOptionnal = Optional.of(session);

        when(sessionRepository.findById(sessionId)).thenReturn(sessionOptionnal);

        assertThrows(BadRequestException.class, () -> sessionServiceUnderTest.noLongerParticipate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

}