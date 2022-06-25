package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Category;
import com.senla.kanapa.entity.FieldFilter;
import com.senla.kanapa.entity.QueryOperator;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.AdvertisementCustomRepository;
import com.senla.kanapa.repository.AdvertisementJpaRepository;
import com.senla.kanapa.repository.CategoryJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.repository.exception.OperationException;
import com.senla.kanapa.service.dto.AdvertisementDto;
import com.senla.kanapa.service.dto.request.FilterDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import com.senla.kanapa.service.security.TokenExtractData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AdvertisementServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AdvertisementServiceImplTest {
    @MockBean
    private AdvertisementCustomRepository advertisementCustomRepository;

    @MockBean
    private AdvertisementJpaRepository advertisementJpaRepository;

    @Autowired
    private AdvertisementServiceImpl advertisementServiceImpl;

    @MockBean
    private CategoryJpaRepository categoryJpaRepository;

    @MockBean
    private TokenExtractData tokenExtractData;

    @MockBean
    private UserJpaRepository userJpaRepository;

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

    private List<Advertisement> getAdvertisementList() {
        Advertisement advertisement = Advertisement.builder()
                .id(1L)
                .name("Cat")
                .category(getCategoryList().get(2))
                .user(getUserList().get(1))
                .description("I'll put the cat in good hands. Positive: the cat has no fleas and is vaccinated. " +
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


    @Test
    void AdvertisementServiceImpl_saveAdvertisement() {
        when(userJpaRepository.getReferenceById(1L)).thenReturn(getUserList().get(0));
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);

        when(advertisementJpaRepository.save((Advertisement) any())).thenReturn(getAdvertisementList().get(0));
        AdvertisementDto advertisementDto = mock(AdvertisementDto.class);
        when(advertisementDto.getCategoryId()).thenReturn(3L);
        when(advertisementDto.getDescription()).thenReturn("Positive: the cat has no fleas and is vaccinated. " +
                "Negative: periodically goes in search of young kitties");
        when(advertisementDto.getName()).thenReturn("Cat");
        when(advertisementDto.getPrice()).thenReturn(BigDecimal.valueOf(1L));
        advertisementServiceImpl.saveAdvertisement(advertisementDto, "EJfksengfpasd");
        verify(userJpaRepository).getReferenceById(anyLong());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(categoryJpaRepository).getReferenceById(anyLong());
        verify(advertisementJpaRepository).save(any());
        verify(advertisementDto).getCategoryId();
        verify(advertisementDto).getDescription();
        verify(advertisementDto).getName();
        verify(advertisementDto).getPrice();
    }

    @Test
    void AdvertisementServiceImpl_editAdvertisement_exceptionCompareToken() {
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        when(advertisementJpaRepository.getReferenceById(anyLong())).thenReturn(getAdvertisementList().get(0));
        assertThrows(TokenCompareException.class,
                () -> advertisementServiceImpl.editAdvertisement(1L, mock(AdvertisementDto.class), "kyfli;ghlk"));
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(advertisementJpaRepository).getReferenceById(anyLong());
    }

    @Test
    void AdvertisementServiceImpl_getAdvertisementByUser() {
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        when(advertisementJpaRepository.getByUserAndDateCloseIsNull(getUserList().get(0))).thenReturn(getAdvertisementList());
        assertEquals(2, advertisementServiceImpl.getAdvertisementByUser(1L).size());
        verify(userJpaRepository).getReferenceById(anyLong());
        verify(advertisementJpaRepository).getByUserAndDateCloseIsNull(any());
    }

    @Test
    void AdvertisementServiceImpl_getAdvertisementOrderByRating_emptyList() {
        when(advertisementJpaRepository.getByDateCloseIsNullOrderByDateBonusDesc()).thenReturn(new ArrayList<>());
        assertTrue(advertisementServiceImpl.getAdvertisementOrderByRating().isEmpty());
        verify(advertisementJpaRepository).getByDateCloseIsNullOrderByDateBonusDesc();
    }

    @Test
    void AdvertisementServiceImpl_getAdvertisementOrderByRating() {
        when(advertisementJpaRepository.getByDateCloseIsNullOrderByDateBonusDesc()).thenReturn(getAdvertisementList());
        assertEquals(2, advertisementServiceImpl.getAdvertisementOrderByRating().size());
        verify(advertisementJpaRepository).getByDateCloseIsNullOrderByDateBonusDesc();
    }

    @Test
    void AdvertisementServiceImpl_getAdvertisementByFilter_emptyList() throws OperationException {
        when(advertisementCustomRepository.getQueryResult(any(), any())).thenReturn(new ArrayList<>());
        assertTrue(
                advertisementServiceImpl.getAdvertisementByFilter(new ArrayList<>(), "FieldSort", Sort.Direction.ASC)
                        .isEmpty());
        verify(advertisementCustomRepository).getQueryResult(any(), any());
    }

    @Test
    void AdvertisementServiceImpl_getAdvertisementByFilter() throws OperationException {
        when(advertisementCustomRepository.getQueryResult(any(), (Sort) any()))
                .thenReturn(getAdvertisementList());
        assertEquals(2,
                advertisementServiceImpl.getAdvertisementByFilter(new ArrayList<>(), "FieldSort", Sort.Direction.ASC)
                        .size());
        verify(advertisementCustomRepository).getQueryResult(any(), any());
    }

    @Test
    void AdvertisementServiceImpl_getAdvertisementByFilter_FilterDto() throws OperationException {
        when(advertisementCustomRepository.getQueryResult(any(), any()))
                .thenReturn(new ArrayList<>());
        FilterDto filterDto = mock(FilterDto.class);
        when(filterDto.getOperator()).thenReturn(QueryOperator.GREATER_THAN);
        when(filterDto.getValue()).thenReturn("42");
        when(filterDto.getKey()).thenReturn(FieldFilter.PRICE);

        ArrayList<FilterDto> filterDtoList = new ArrayList<>();
        filterDtoList.add(filterDto);
        assertTrue(advertisementServiceImpl.getAdvertisementByFilter(filterDtoList, "FieldSort", Sort.Direction.ASC)
                .isEmpty());
        verify(advertisementCustomRepository).getQueryResult(any(), any());
        verify(filterDto).getKey();
        verify(filterDto).getOperator();
        verify(filterDto).getValue();
    }
}

