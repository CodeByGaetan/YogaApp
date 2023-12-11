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

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

@ExtendWith(MockitoExtension.class)
public class UserMapperUnitTest {

    @InjectMocks
    private UserMapper userMapperUnderTest = Mappers.getMapper(UserMapper.class);

    User user;
    private UserDto userDto;

    @BeforeEach
    public void init() {
        Long userId = 1L;
        String username = "yoga@studio.com";
        String lastName = "Studio";
        String firstName = "Yoga";
        String password = "test!1234";
        Boolean admin = true;

        user = new User();
        user.setId(userId);
        user.setEmail(username);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        user.setAdmin(admin);

        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setEmail(username);
        userDto.setLastName(lastName);
        userDto.setFirstName(firstName);
        userDto.setPassword(password);
        userDto.setAdmin(admin);
    }

    @Test
    public void convert_user_toDto() {
        UserDto testUserDto = userMapperUnderTest.toDto(user);

        assertEquals(userDto, testUserDto);
    }

    @Test
    public void convert_null_toDto() {
        User user = null;

        UserDto testUserDto = userMapperUnderTest.toDto(user);

        assertNull(testUserDto);
    }

    @Test
    public void convert_userDto_toEntity() {
        User testUser = userMapperUnderTest.toEntity(userDto);

        assertEquals(user, testUser);
    }

    @Test
    public void convert_null_toEntity() {
        UserDto userDto = null;

        User testUser = userMapperUnderTest.toEntity(userDto);

        assertNull(testUser);
    }

    @Test
    public void convert_userList_toDto() {
        List<User> userList = new ArrayList<>();
        userList.add(user);

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);

        List<UserDto> testUserDtoList = userMapperUnderTest.toDto(userList);

        assertEquals(userDtoList, testUserDtoList);
    }

    @Test
    public void convert_nullList_toDto() {
        List<User> userList = null;

        List<UserDto> testUserDtoList = userMapperUnderTest.toDto(userList);

        assertNull(testUserDtoList);
    }

    @Test
    public void convert_userDtoList_toEntity() {
        List<User> userList = new ArrayList<>();
        userList.add(user);

        List<UserDto> userDtoList = new ArrayList<>();
        userDtoList.add(userDto);

        List<User> testUserList = userMapperUnderTest.toEntity(userDtoList);

        assertEquals(userList, testUserList);
    }

    @Test
    public void convert_nullList_toEntity() {
        List<UserDto> userList = null;

        List<User> testUserList = userMapperUnderTest.toEntity(userList);

        assertNull(testUserList);
    }
    
}
