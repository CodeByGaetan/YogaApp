package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

@ExtendWith(MockitoExtension.class)
public class SessionMapperUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private SessionMapper sessionMapperUnderTest = Mappers.getMapper(SessionMapper.class);

    private Teacher teacher;
    private User user1;
    private User user2;
    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    public void init() {

        teacher = new Teacher();
        teacher.setId(5L);

        user1 = new User();
        user1.setId(2L);
        user2 = new User();
        user2.setId(3L);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Long sessionId = 1L;
        String name = "ChiuKong";
        String description = "Cour de ChiuKong face au vent";
        Date date = new Date();

        session = new Session();
        session.setId(sessionId);
        session.setName(name);
        session.setDescription(description);
        session.setDate(date);
        session.setTeacher(teacher);
        session.setUsers(users);

        List<Long> usersId = new ArrayList<>();
        usersId.add(user1.getId());
        usersId.add(user2.getId());

        sessionDto = new SessionDto();
        sessionDto.setId(sessionId);
        sessionDto.setName(name);
        sessionDto.setDescription(description);
        sessionDto.setDate(date);
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(usersId);
    }

    @Test
    public void convert_session_toDto() {
        SessionDto testSessionDto = sessionMapperUnderTest.toDto(session);

        assertEquals(sessionDto, testSessionDto);
    }

    @Test
    public void convert_null_toDto() {
        Session session = null;

        SessionDto testSessionDto = sessionMapperUnderTest.toDto(session);

        assertNull(testSessionDto);
    }

    @Test
    public void convert_sessionWithTeacherNull_toDto() {
        session.setTeacher(null);

        SessionDto testSessionDto = sessionMapperUnderTest.toDto(session);
        assertNull(testSessionDto.getTeacher_id());
    }

    @Test
    public void convert_sessionWithTeacherIdNull_toDto() {
        session.getTeacher().setId(null);

        SessionDto testSessionDto = sessionMapperUnderTest.toDto(session);
        assertNull(testSessionDto.getTeacher_id());
    }



    @Test
    public void convert_sessionDto_toEntity() {

        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(userService.findById(user1.getId())).thenReturn(user1);
        when(userService.findById(user2.getId())).thenReturn(user2);
        
        Session testSession = sessionMapperUnderTest.toEntity(sessionDto);

        verify(teacherService).findById(teacher.getId());
        verify(userService).findById(user1.getId());
        verify(userService).findById(user2.getId());
        assertEquals(session, testSession);
    }

    @Test
    public void convert_null_toEntity() {
        SessionDto sessionDto = null;

        Session testSession = sessionMapperUnderTest.toEntity(sessionDto);

        assertNull(testSession);
    }

    @Test
    public void convert_sessionList_toDto() {

        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        List<SessionDto> sessionDtoList = new ArrayList<>();
        sessionDtoList.add(sessionDto);

        List<SessionDto> testSessionDtoList = sessionMapperUnderTest.toDto(sessionList);

        assertEquals(sessionDtoList, testSessionDtoList);
    }

    @Test
    public void convert_nullList_toDto() {
        List<Session> sessionList = null;

        List<SessionDto> testSessionDtoList = sessionMapperUnderTest.toDto(sessionList);

        assertNull(testSessionDtoList);
    }

    @Test
    public void convert_sessionDtoList_toEntity() {

        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);

        List<SessionDto> sessionDtoList = new ArrayList<>();
        sessionDtoList.add(sessionDto);

        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(userService.findById(user1.getId())).thenReturn(user1);
        when(userService.findById(user2.getId())).thenReturn(user2);
        
        List<Session> testSessionList = sessionMapperUnderTest.toEntity(sessionDtoList);

        verify(teacherService).findById(teacher.getId());
        verify(userService).findById(user1.getId());
        verify(userService).findById(user2.getId());
        assertEquals(sessionList, testSessionList);
    }

    @Test
    public void convert_nullList_toEntity() {
        List<SessionDto> sessionDtoList = null;

        List<Session> testSessionList = sessionMapperUnderTest.toEntity(sessionDtoList);

        assertNull(testSessionList);
    }

}