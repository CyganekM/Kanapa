package com.senla.kanapa.service.mapper;

import com.senla.kanapa.entity.User;
import com.senla.kanapa.service.dto.request.UserAddDto;
import com.senla.kanapa.service.dto.request.UserEditDto;
import com.senla.kanapa.service.dto.response.UserContactDto;
import com.senla.kanapa.service.dto.response.UserDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public User convertUserAddDtoToUser(UserAddDto userAddDto) {
        return User.builder()
                .username(userAddDto.getLogin())
                .password(userAddDto.getPassword())
                .name(userAddDto.getName())
                .surname(userAddDto.getSurname())
                .birthdate(userAddDto.getBirthdate())
                .phone(userAddDto.getPhone())
                .email(userAddDto.getEmail())
                .build();
    }

    public void convertUserEditDtoToUser(UserEditDto userEditDto, User user) {
        user.setName(userEditDto.getName());
        user.setUsername(userEditDto.getLogin());
        user.setSurname(userEditDto.getSurname());
        user.setPhone(userEditDto.getPhone());
        user.setEmail(userEditDto.getEmail());
        user.setBirthdate(userEditDto.getBirthdate());
    }

    public UserDto convertUserToDto(User user) {
        return UserDto.builder()
                .login(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .birthdate(user.getBirthdate())
                .phone(user.getPhone())
                .email(user.getEmail())
                .bonus(user.getKanapic())
                .build();
    }

    public UserContactDto convertUserToUserContactDto(User user) {
        return UserContactDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}
