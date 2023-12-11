package com.openclassrooms.starterjwt.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerUnitTest {

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherControllerUnderTest;

    private Long teacherId = 1L;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    public void init() {

        teacher = new Teacher();
        teacher.setId(teacherId);
    
        teacherDto = new TeacherDto();
        teacherDto.setId(teacherId);
    }


    @Test
    public void find_teacherById_returnOk() {
        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response =  teacherControllerUnderTest.findById(String.valueOf(teacherId));

        verify(teacherService).findById(teacherId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDto, response.getBody());
    }

    @Test
    public void find_teacherById_returnNotFound() {
        when(teacherService.findById(teacherId)).thenReturn(null);

        ResponseEntity<?> response =  teacherControllerUnderTest.findById(String.valueOf(teacherId));

        verify(teacherService).findById(teacherId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void find_teacherById_returnBadRequest() {
        when(teacherService.findById(teacherId)).thenThrow(new NumberFormatException());

        ResponseEntity<?> response =  teacherControllerUnderTest.findById(String.valueOf(teacherId));

        verify(teacherService).findById(teacherId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void find_teachers_returnOk() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);

        List<TeacherDto> teachersDto = new ArrayList<>();
        teachersDto.add(teacherDto);
        
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teachersDto);

        ResponseEntity<?> response =  teacherControllerUnderTest.findAll();

        verify(teacherService).findAll();
        verify(teacherMapper).toDto(teachers);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teachersDto, response.getBody());
    }


}
