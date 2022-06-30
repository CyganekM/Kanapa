package com.senla.kanapa.controller;

import com.senla.kanapa.service.UserService;
import com.senla.kanapa.service.dto.request.UserAddDto;
import com.senla.kanapa.service.dto.request.UserEditDto;
import com.senla.kanapa.service.dto.request.UserPasswordDto;
import com.senla.kanapa.service.dto.response.UserContactDto;
import com.senla.kanapa.service.dto.response.UserDto;
import com.senla.kanapa.service.exception.ChangePasswordException;
import com.senla.kanapa.service.exception.TokenCompareException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Gets all users")
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    @GetMapping("/advertisement")
    @Operation(summary = "Can only be done by the user who made the transaction (confirmed by the seller)")
    public ResponseEntity<UserContactDto> getUserByAdvertisementId(@RequestParam Long advertisementId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok().body(userService.getUserByAdvertisementId(advertisementId, token));
    }

    @PostMapping
    @Operation(summary = "Add a new user")
    @ResponseStatus(HttpStatus.OK)
    public void addUser(@RequestBody @Valid UserAddDto userAddDto) {
        userService.saveUser(userAddDto);
    }

    @PutMapping
    @Operation(summary = "Edit a user")
    @ResponseStatus(HttpStatus.OK)
    public void editUser(@RequestBody @Valid UserEditDto userEditDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws TokenCompareException {
        userService.editUser(userEditDto, token);
    }

    @PutMapping("/password")
    @Operation(summary = "Change Password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody @Valid UserPasswordDto userPasswordDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws ChangePasswordException {
        userService.changePassword(userPasswordDto, token);
    }

    @PutMapping("/{userId}/kanapic")
    @Operation(summary = "Add kanapics to the user's account")
    @ResponseStatus(HttpStatus.OK)
    public void addBonus(@PathVariable Long userId, @RequestParam("kanapic") Integer kanapic) {
        userService.addBonus(userId, kanapic);
    }

    @PutMapping("/{userId}/admin_role")
    @Operation(summary = "Add an administrator role")
    @ResponseStatus(HttpStatus.OK)
    public void addAdminRole(@PathVariable Long userId) {
        userService.addAdminRole(userId);
    }

    @PostMapping(path = "/exit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        userService.addTokenInBlackList(token);
        return ResponseEntity.ok().body("You are logged out");
    }
}
