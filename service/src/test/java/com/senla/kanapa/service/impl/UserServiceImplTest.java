package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Category;
import com.senla.kanapa.entity.Role;
import com.senla.kanapa.entity.Transaction;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.AdvertisementJpaRepository;
import com.senla.kanapa.repository.RoleJpaRepository;
import com.senla.kanapa.repository.TokenBlackListJpaRepository;
import com.senla.kanapa.repository.TransactionJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.dto.request.UserAddDto;
import com.senla.kanapa.service.dto.request.UserEditDto;
import com.senla.kanapa.service.dto.request.UserPasswordDto;
import com.senla.kanapa.service.dto.response.UserContactDto;
import com.senla.kanapa.service.exception.ChangePasswordException;
import com.senla.kanapa.service.exception.TokenCompareException;
import com.senla.kanapa.service.security.TokenExtractData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private AdvertisementJpaRepository advertisementJpaRepository;

    @MockBean
    private RoleJpaRepository roleJpaRepository;

    @MockBean
    private TokenExtractData tokenExtractData;

    @MockBean
    private TransactionJpaRepository transactionJpaRepository;

    @MockBean
    private UserJpaRepository userJpaRepository;

    @MockBean
    private TokenBlackListJpaRepository tokenBlackListJpaRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    private List<Advertisement> getAdvertisementList() {
        Advertisement advertisement = Advertisement.builder()
                .id(1L)
                .name("Cat")
                .category(getCategoryList().get(2))
                .user(getUserList().get(1))
                .description("I'll put the cat boys in good hands. Positive: the cat has no fleas and is vaccinated. " +
                        "Negative: periodically goes in search of young kitties")
                .dateStart(LocalDateTime.of(2022, 06, 14, 20, 20))
                .price(BigDecimal.valueOf(1L))
                .build();
        Advertisement advertisement1 = Advertisement.builder()
                .id(2L)
                .name("Dog")
                .category(getCategoryList().get(1))
                .user(getUserList().get(0))
                .description("Big boy")
                .dateStart(LocalDateTime.of(2022, 06, 15, 20, 20))
                .price(BigDecimal.valueOf(100L))
                .build();
        List<Advertisement> advertisementList = new ArrayList<>();
        advertisementList.add(advertisement);
        advertisementList.add(advertisement1);
        return advertisementList;
    }

    private Transaction getTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAdvertisement(getAdvertisementList().get(0));
        transaction.setCustomer(getUserList().get(1));
        transaction.setCustomerFlag(true);
        transaction.setDate(LocalDateTime.of(1, 1, 1, 1, 1));
        transaction.setId(1L);
        transaction.setRating(10);
        return transaction;
    }

    private List<Category> getCategoryList() {
        Category category = new Category();
        category.setDescription("The characteristics of someone or something");
        category.setId(1L);
        category.setName("Animal");

        Category category1 = new Category();
        category1.setChildCategories(new ArrayList<>());
        category1.setDescription("Different dogs");
        category1.setId(2L);
        category1.setName("Dogs");
        category1.setParentCategory(category);

        Category category2 = new Category();
        category2.setDescription("Different cats");
        category2.setId(3L);
        category2.setName("Cats");
        category2.setParentCategory(category);

        List<Category> categories = new ArrayList<>();
        categories.add(category);
        categories.add(category1);
        categories.add(category2);

        return categories;
    }

    private List<User> getUserList() {
        User user = new User();
        user.setBirthdate(LocalDate.ofEpochDay(1L));
        user.setDateRegistration(LocalDateTime.of(1980, 1, 1, 11, 15));
        user.setEmail("maximus@tut.by");
        user.setEnabled(true);
        user.setId(10L);
        user.setKanapic(20);
        user.setName("John");
        user.setPassword("VeryVeryWellPassword123");
        user.setPhone("21528");
        user.setRating(10.0d);
        user.setRoles(new ArrayList<>());
        user.setSurname("Doe");
        user.setUsername("janedoe");
        User user1 = new User();
        user1.setBirthdate(LocalDate.ofEpochDay(1L));
        user1.setDateRegistration(LocalDateTime.of(1980, 1, 1, 11, 15));
        user1.setEmail("maximus@tut.by");
        user1.setEnabled(true);
        user1.setId(10L);
        user1.setKanapic(20);
        user1.setName("John");
        user1.setPassword("VeryVeryWellPassword123");
        user1.setPhone("21528");
        user1.setRating(10.0d);
        user1.setRoles(new ArrayList<>());
        user1.setSurname("Doe");
        user1.setUsername("janedoe");

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        return users;
    }

    @Test
    void UserServiceImpl_changePassword_exception() {
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        UserPasswordDto userPasswordDto = new UserPasswordDto();
        userPasswordDto.setPasswordNew("PasswordNew");
        userPasswordDto.setPasswordOld("PasswordOld");
        userPasswordDto.setUserId(1L);
        assertThrows(ChangePasswordException.class, () -> userServiceImpl.changePassword(userPasswordDto, "Bearer eyJ0eXAiOiJKV1QiLCJ"));
        verify(userJpaRepository).getReferenceById(anyLong());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
    }

    @Test
    void UserServiceImpl_addBonus() {
        when(userJpaRepository.save(any())).thenReturn(getUserList().get(0));
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        userServiceImpl.addBonus(1L, 2);
        verify(userJpaRepository).getReferenceById(anyLong());
        verify(userJpaRepository).save(any());
    }


    @Test
    void UserServiceImpl_addBonus_UsernameNotFoundException() {
        when(userJpaRepository.save((User) any())).thenThrow(new UsernameNotFoundException("Msg"));
        when(userJpaRepository.getReferenceById((anyLong()))).thenReturn(getUserList().get(0));
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.addBonus(123L, 2));
        verify(userJpaRepository).getReferenceById(anyLong());
        verify(userJpaRepository).save(any());
    }

    @Test
    void UserServiceImpl_editUser() throws TokenCompareException {
        when(userJpaRepository.save(any())).thenReturn(getUserList().get(0));
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        UserEditDto userEditDto = mock(UserEditDto.class);
        when(userEditDto.getEmail()).thenReturn("user@tut.by");
        when(userEditDto.getLogin()).thenReturn("Login");
        when(userEditDto.getName()).thenReturn("Name");
        when(userEditDto.getPhone()).thenReturn("2020202020");
        when(userEditDto.getSurname()).thenReturn("Doe");
        when(userEditDto.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1L));
        userServiceImpl.editUser(userEditDto, "ABC123");
        verify(userJpaRepository).getReferenceById(anyLong());
        verify(userJpaRepository).save(any());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(userEditDto).getEmail();
        verify(userEditDto).getLogin();
        verify(userEditDto).getName();
        verify(userEditDto).getPhone();
        verify(userEditDto).getSurname();
        verify(userEditDto).getBirthdate();
    }


    @Test
    void UserServiceImpl_saveUser() {
        when(userJpaRepository.save(any())).thenReturn(getUserList().get(0));
        Role role = new Role();
        role.setAuthority("JaneDoe");
        role.setId(123L);
        when(roleJpaRepository.getReferenceById(any())).thenReturn(role);
        UserAddDto userAddDto = mock(UserAddDto.class);
        when(userAddDto.getEmail()).thenReturn("user@tut.by");
        when(userAddDto.getLogin()).thenReturn("Login");
        when(userAddDto.getName()).thenReturn("Name");
        when(userAddDto.getPhone()).thenReturn("2020202020");
        when(userAddDto.getSurname()).thenReturn("Doe");
        when(userAddDto.getBirthdate()).thenReturn(LocalDate.ofEpochDay(1L));
        doNothing().when(userAddDto).setPassword((anyString()));
        when(userAddDto.getPassword()).thenReturn("password");
        userServiceImpl.saveUser(userAddDto);
        verify(userJpaRepository).save(any());
        verify(roleJpaRepository).getReferenceById(any());
        verify(userAddDto).getEmail();
        verify(userAddDto).getLogin();
        verify(userAddDto).getName();
        verify(userAddDto, atLeast(1)).getPassword();
        verify(userAddDto).getPhone();
        verify(userAddDto).getSurname();
        verify(userAddDto).getBirthdate();
        verify(userAddDto).setPassword(any());
    }


    @Test
    void UserServiceImpl_findAllUsers_empty() {
        when(userJpaRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(userServiceImpl.findAllUsers().isEmpty());
        verify(userJpaRepository).findAll();
    }

    @Test
    void UserServiceImpl_findAllUsers_sizeList() {
        when(userJpaRepository.findAll()).thenReturn(getUserList());
        assertEquals(2, userServiceImpl.findAllUsers().size());
        verify(userJpaRepository).findAll();
    }

    @Test
    void UserServiceImpl_findAllUsers_UsernameNotFoundException() {
        when(userJpaRepository.findAll()).thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.findAllUsers());
        verify(userJpaRepository).findAll();
    }

    @Test
    void UserServiceImpl_addAdminRole() {
        Role role = new Role();
        role.setAuthority("ROLE_ADMIN");
        role.setId(1L);
        when(userJpaRepository.save(any())).thenReturn(getUserList().get(0));
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        when(roleJpaRepository.getByAuthority(anyString())).thenReturn(role);
        userServiceImpl.addAdminRole(1L);
        verify(userJpaRepository).getReferenceById(anyLong());
        verify(userJpaRepository).save(any());
        verify(roleJpaRepository).getByAuthority(anyString());
    }

    @Test
    void UserServiceImpl_addAdminRole_usernameNotFoundException() {
        when(userJpaRepository.save((User) any())).thenReturn(getUserList().get(0));
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        when(roleJpaRepository.getByAuthority(anyString())).thenThrow(new UsernameNotFoundException("ROLE_ADMIN"));
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.addAdminRole(123L));
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(roleJpaRepository).getByAuthority((String) any());
    }


    @Test
    void UserServiceImpl_getUserByAdvertisementId() {
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        when(transactionJpaRepository.save(any())).thenReturn(getTransaction());
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        when(advertisementJpaRepository.getReferenceById(anyLong())).thenReturn(getAdvertisementList().get(1));
        UserContactDto actualUserByAdvertisementId = userServiceImpl.getUserByAdvertisementId(1L, "ABC123");
        assertEquals("maximus@tut.by", actualUserByAdvertisementId.getEmail());
        assertEquals("Doe", actualUserByAdvertisementId.getSurname());
        assertEquals("21528", actualUserByAdvertisementId.getPhone());
        assertEquals("John", actualUserByAdvertisementId.getName());
        verify(userJpaRepository).getReferenceById(anyLong());
        verify(transactionJpaRepository).save(any());
        verify(tokenExtractData).extractUserIdFromToken((anyString()));
        verify(advertisementJpaRepository).getReferenceById(anyLong());
    }

    @Test
    void UserServiceImpl_getUserByAdvertisementId_usernameNotFoundException() {
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        when(transactionJpaRepository.save(any())).thenReturn(getTransaction());
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        when(advertisementJpaRepository.getReferenceById(anyLong()))
                .thenThrow(new UsernameNotFoundException("Msg"));
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.getUserByAdvertisementId(123L, "ABC123"));
        verify(advertisementJpaRepository).getReferenceById((Long) any());
    }

    @Test
    void UserServiceImpl_loadUserByUsername() throws UsernameNotFoundException {
        User user = getUserList().get(0);
        when(userJpaRepository.getUserByUsername(anyString())).thenReturn(user);
        assertSame(user, userServiceImpl.loadUserByUsername("Login"));
        verify(userJpaRepository).getUserByUsername(anyString());
    }

    @Test
    void UserServiceImpl_loadUserByUsername_usernameNotFoundException() throws UsernameNotFoundException {
        when(userJpaRepository.getUserByUsername(anyString()))
                .thenThrow(new UsernameNotFoundException("User '%s' not found"));
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.loadUserByUsername("Login"));
        verify(userJpaRepository).getUserByUsername((String) any());
    }
}

