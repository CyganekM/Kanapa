package com.senla.kanapa.service;

import com.senla.kanapa.service.dto.request.UserAddDto;
import com.senla.kanapa.service.dto.request.UserEditDto;
import com.senla.kanapa.service.dto.request.UserPasswordDto;
import com.senla.kanapa.service.dto.response.UserContactDto;
import com.senla.kanapa.service.dto.response.UserDto;
import com.senla.kanapa.service.exception.ChangePasswordException;
import com.senla.kanapa.service.exception.TokenCompareException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    void changePassword(UserPasswordDto userPasswordDto, String token) throws ChangePasswordException;

    void addBonus(Long userId, Integer bonus);

    void editUser(UserEditDto userEditDto, String token) throws TokenCompareException;

    void saveUser(UserAddDto userAddDto);

    List<UserDto> findAllUsers();

    void addAdminRole(Long userId);

    UserContactDto getUserByAdvertisementId(Long advertisementId, String token);

    void addTokenInBlackList(String token);
}
