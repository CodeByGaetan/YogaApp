package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceUnitTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherServiceUnderTest;

    private Long teacherId = 1L;
    private Teacher teacher;

    @BeforeEach
    public void init() {
        teacher = new Teacher();
        teacher.setId(teacherId);
    }

    @Test
    public void find_teachers_returnTeachers() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);

        when(teacherRepository.findAll()).thenReturn(teacherList);

        teacherServiceUnderTest.findAll();

        verify(teacherRepository).findAll();
    }

    @Test
    public void find_teacherById_returnTeacher() {
        Optional<Teacher> teacherOptional = Optional.of(teacher);

        when(teacherRepository.findById(teacherId)).thenReturn(teacherOptional);

        Teacher testTeacher = teacherServiceUnderTest.findById(teacherId);

        verify(teacherRepository).findById(teacherId);
        assertEquals(teacher, testTeacher);
    }

    @Test
    public void find_teacherById_returnNull() {
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        Teacher testTeacher = teacherServiceUnderTest.findById(teacherId);

        verify(teacherRepository).findById(teacherId);
        assertNull(testTeacher);
    }
}
