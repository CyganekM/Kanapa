package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Category;
import com.senla.kanapa.entity.Kanapic;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.AdvertisementJpaRepository;
import com.senla.kanapa.repository.KanapikJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.dto.request.KanapicCreditDto;
import com.senla.kanapa.service.dto.request.KanapicDebitDto;
import com.senla.kanapa.service.security.TokenExtractData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {KanapicServiceImpl.class})
@ExtendWith(SpringExtension.class)
class KanapicServiceImplTest {
    @MockBean
    private AdvertisementJpaRepository advertisementJpaRepository;

    @Autowired
    private KanapicServiceImpl kanapicServiceImpl;

    @MockBean
    private KanapikJpaRepository kanapikJpaRepository;

    @MockBean
    private TokenExtractData tokenExtractData;

    @MockBean
    private UserJpaRepository userJpaRepository;

    @MockBean
    private KanapicDebitDto kanapicDebitDto;

    @MockBean
    private KanapicCreditDto kanapicCreditDto;

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

    private Kanapic getKanapic() {
        Kanapic kanapic = new Kanapic();
        kanapic.setAdvertisement(getAdvertisementList().get(0));
        kanapic.setCredit(1);
        kanapic.setDate(LocalDateTime.of(1, 1, 1, 1, 1));
        kanapic.setDebit(1);
        kanapic.setId(1L);
        kanapic.setUser(getUserList().get(0));
        return kanapic;
    }

    @Test
    void AdvertisementJpaRepository_addKanapicDebit() {
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        when(kanapikJpaRepository.save(any())).thenReturn(getKanapic());
        when(kanapicDebitDto.getDebit()).thenReturn(1);
        when(kanapicDebitDto.getUserId()).thenReturn(123L);
        kanapicServiceImpl.creditKanapic(kanapicDebitDto);
        verify(userJpaRepository).getReferenceById((Long) any());
        verify(kanapikJpaRepository).save((Kanapic) any());
        verify(kanapicDebitDto, atLeast(1)).getDebit();
        verify(kanapicDebitDto).getUserId();
    }

    @Test
    void AdvertisementJpaRepository_addKanapicCredit() throws Exception {
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        when(kanapikJpaRepository.save(any())).thenReturn(getKanapic());
        when(advertisementJpaRepository.getReferenceById(anyLong())).thenReturn(getAdvertisementList().get(0));
        when(kanapicCreditDto.getCredit()).thenReturn(1);
        when(kanapicCreditDto.getAdvertisementId()).thenReturn(1L);
        kanapicServiceImpl.debitKanapic(kanapicCreditDto, "ABC123");
        verify(userJpaRepository).getReferenceById(anyLong());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(kanapikJpaRepository).save(any());
        verify(advertisementJpaRepository).getReferenceById(anyLong());
        verify(kanapicCreditDto, atLeast(1)).getCredit();
        verify(kanapicCreditDto).getAdvertisementId();
    }
}

