package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;

@ExtendWith(MockitoExtension.class)
public class TeacherMapperUnitTest {

    @InjectMocks
    private TeacherMapper teacherMapperUnderTest = Mappers.getMapper(TeacherMapper.class);

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    public void init() {
        Long teacherId = 1L;
        String lastName = "NOM";
        String firstName = "Prenom";

        teacher = new Teacher();
        teacher.setId(teacherId);
        teacher.setLastName(lastName);
        teacher.setFirstName(firstName);

        teacherDto = new TeacherDto();
        teacherDto.setId(teacherId);
        teacherDto.setLastName(lastName);
        teacherDto.setFirstName(firstName);
    }

    @Test
    public void convert_teacher_toDto() {
        TeacherDto testTeacherDto = teacherMapperUnderTest.toDto(teacher);

        assertEquals(teacherDto, testTeacherDto);
    }

    @Test
    public void convert_null_toDto() {
        Teacher teacher = null;

        TeacherDto testTeacherDto = teacherMapperUnderTest.toDto(teacher);

        assertNull(testTeacherDto);
    }

    @Test
    public void convert_teacherDto_toEntity() {
        Teacher testTeacher = teacherMapperUnderTest.toEntity(teacherDto);

        assertEquals(teacher, testTeacher);
    }

    @Test
    public void convert_null_toEntity() {
        TeacherDto teacherDto = null;

        Teacher testTeacher = teacherMapperUnderTest.toEntity(teacherDto);

        assertNull(testTeacher);
    }

    @Test
    public void convert_teacherList_toDto() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);

        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teacherDtoList.add(teacherDto);

        List<TeacherDto> testTeacherDtoList = teacherMapperUnderTest.toDto(teacherList);

        assertEquals(teacherDtoList, testTeacherDtoList);
    }

    @Test
    public void convert_nullList_toDto() {
        List<Teacher> teacherList = null;

        List<TeacherDto> testTeacherDtoList = teacherMapperUnderTest.toDto(teacherList);

        assertNull(testTeacherDtoList);
    }

    @Test
    public void convert_teacherDtoList_toEntity() {
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(teacher);

        List<TeacherDto> teacherDtoList = new ArrayList<>();
        teacherDtoList.add(teacherDto);

        List<Teacher> testTeacherList = teacherMapperUnderTest.toEntity(teacherDtoList);

        assertEquals(teacherList, testTeacherList);
    }

    @Test
    public void convert_nullList_toEntity() {
        List<TeacherDto> teacherDtoList = null;

        List<Teacher> testTeacherList = teacherMapperUnderTest.toEntity(teacherDtoList);

        assertNull(testTeacherList);
    }

}