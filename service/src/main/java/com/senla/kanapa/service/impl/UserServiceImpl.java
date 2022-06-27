package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Role;
import com.senla.kanapa.entity.TokenBlack;
import com.senla.kanapa.entity.Transaction;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.AdvertisementJpaRepository;
import com.senla.kanapa.repository.RoleJpaRepository;
import com.senla.kanapa.repository.TokenBlackListJpaRepository;
import com.senla.kanapa.repository.TransactionJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.UserService;
import com.senla.kanapa.service.dto.request.UserAddDto;
import com.senla.kanapa.service.dto.request.UserEditDto;
import com.senla.kanapa.service.dto.request.UserPasswordDto;
import com.senla.kanapa.service.dto.response.UserContactDto;
import com.senla.kanapa.service.dto.response.UserDto;
import com.senla.kanapa.service.exception.ChangePasswordException;
import com.senla.kanapa.service.exception.TokenCompareException;
import com.senla.kanapa.service.mapper.UserMapper;
import com.senla.kanapa.service.security.JwtAuthorizationFilter;
import com.senla.kanapa.service.security.TokenExtractData;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private static final Integer KANAPIC = 10;
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final TransactionJpaRepository transactionJpaRepository;
    private final AdvertisementJpaRepository advertisementJpaRepository;
    private final TokenExtractData tokenExtractData;
    private final TokenBlackListJpaRepository tokenBlackListJpaRepository;


    @Override
    @Transactional
    public void changePassword(UserPasswordDto userPasswordDto, String token) throws ChangePasswordException {
        User user = userJpaRepository.getReferenceById(tokenExtractData.extractUserIdFromToken(token));
        log.info("Attempt to change the user's password {}.", user.getUsername());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(userPasswordDto.getPasswordOld(), user.getPassword())) {
            String passwordNew = new BCryptPasswordEncoder().encode(userPasswordDto.getPasswordNew());
            user.setPassword(passwordNew);
            userJpaRepository.save(user);
            JwtAuthorizationFilter.TOKEN_BLACK.add(token);
            log.info("The password of the user with the login - {} has been changed successfully", user.getUsername());
        } else {
            throw new ChangePasswordException("An error occurred when changing the password " + user.getUsername());
        }
    }

    @Override
    @Secured("ROLE_ADMIN")
    @Transactional
    public void addBonus(Long userId, Integer kanapic) {
        log.info("Start adding KANAPIK");
        User user = userJpaRepository.getReferenceById(userId);
        if (user.getKanapic() != null) {
            user.setKanapic(kanapic + user.getKanapic());
        } else {
            user.setKanapic(kanapic);
        }
        userJpaRepository.save(user);
        log.info("The administrator added {} KANAPIK to the user's account Id = {}", kanapic, userId);
    }

    @Override
    @Transactional
    public void editUser(UserEditDto userEditDto, String token) throws TokenCompareException {
        Long userId = tokenExtractData.extractUserIdFromToken(token);
        log.info("Start editing user profile Id = {}", userId);
        User user = userJpaRepository.getReferenceById(userId);
        UserMapper.convertUserEditDtoToUser(userEditDto, user);
        userJpaRepository.save(user);
        log.info("End of editing the user profile Id = {}", userId);
    }

    @Override
    @Transactional
    public void saveUser(UserAddDto userAddDto) {
        log.info("Start saving user profile");
        String password = new BCryptPasswordEncoder().encode(userAddDto.getPassword());
        userAddDto.setPassword(password);
        List<Role> roles = new ArrayList<>();
        roles.add(roleJpaRepository.getReferenceById(1L));
        User user = UserMapper.convertUserAddDtoToUser(userAddDto);
        user.setEnabled(true);
        user.setRoles(roles);
        user.setEnabled(true);
        user.setKanapic(KANAPIC);
        user.setDateRegistration(LocalDateTime.now());
        userJpaRepository.save(user);
        log.info("End saving user profile");
    }

    @Override
    @Transactional
    public List<UserDto> findAllUsers() {
        List<User> users = userJpaRepository.findAll();
        return users.stream().map(UserMapper::convertUserToDto).collect(Collectors.toList());
    }

    @Override
    public void addAdminRole(Long userId) {
        User user = userJpaRepository.getReferenceById(userId);
        user.getRoles().add(roleJpaRepository.getByAuthority(ROLE_ADMIN));
        userJpaRepository.save(user);
        log.info("The administrator added the {} to the user {} ", ROLE_ADMIN, user.getUsername());
    }

    @Override
    public UserContactDto getUserByAdvertisementId(Long advertisementId, String token) {
        Advertisement advertisement = advertisementJpaRepository.getReferenceById(advertisementId);
        User customer = userJpaRepository.getReferenceById(tokenExtractData.extractUserIdFromToken(token));
        Transaction transaction = Transaction.builder()
                .date(LocalDateTime.now())
                .advertisement(advertisement)
                .customer(customer)
                .customerFlag(false)
                .build();
        transactionJpaRepository.save(transaction);
        return UserMapper.convertUserToUserContactDto(advertisement.getUser());
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userJpaRepository.getUserByUsername(login);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", login));
        }
        return user;
    }

    @Override
    public void addTokenInBlackList(String token) {
        Long userId = tokenExtractData.extractUserIdFromToken(token);
        TokenBlack tokenBlack = TokenBlack.builder().token(token).build();
        tokenBlackListJpaRepository.save(tokenBlack);
        JwtAuthorizationFilter.TOKEN_BLACK.add(token);
        log.info(" The user Id {} exited the program", userId);
    }
}
