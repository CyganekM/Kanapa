package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Category;
import com.senla.kanapa.entity.Transaction;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.TransactionJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.dto.TransactionDto;
import com.senla.kanapa.service.exception.TokenCompareException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TransactionServiceImpl.class})
@ExtendWith(SpringExtension.class)
class TransactionServiceImplTest {
    @MockBean
    private TokenExtractData tokenExtractData;

    @MockBean
    private TransactionJpaRepository transactionJpaRepository;

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;

    @MockBean
    private UserJpaRepository userJpaRepository;

    private List<User> getUserList() {
        User user = new User();
        user.setBirthdate(LocalDate.ofEpochDay(1L));
        user.setDateRegistration(LocalDateTime.of(1980, 1, 1, 11, 15));
        user.setEmail("maximus@tut.by");
        user.setEnabled(true);
        user.setId(1L);
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
        user1.setId(2L);
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

    @Test
    void TransactionServiceImpl_setCustomerFlag_exceptionCompareToken() {
        when(transactionJpaRepository.getReferenceById(anyLong())).thenReturn(getTransaction());
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        assertThrows(TokenCompareException.class, () -> transactionServiceImpl.setCustomerFlag("Bearer eyJ0eXAiOiJKV1", 123L));
        verify(transactionJpaRepository).getReferenceById(anyLong());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
    }

    @Test
    void TransactionServiceImpl_getTransactionsSeller_empty() {
        when(transactionJpaRepository.getByAdvertisement_User_IdAndCustomerFlag(anyLong(), anyBoolean()))
                .thenReturn(new ArrayList<>());
        when(tokenExtractData.extractUserIdFromToken((String) any())).thenReturn(1L);
        assertTrue(transactionServiceImpl.getTransactionsSeller("Bearer eyJ0eXAiOiJKV1", true).isEmpty());
        verify(transactionJpaRepository).getByAdvertisement_User_IdAndCustomerFlag(anyLong(), anyBoolean());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
    }

    @Test
    void TransactionServiceImpl_getTransactionsSeller() {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        transactionList.add(getTransaction());
        when(transactionJpaRepository.getByAdvertisement_User_IdAndCustomerFlag(anyLong(), anyBoolean()))
                .thenReturn(transactionList);
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        List<TransactionDto> actualTransactionsSeller = transactionServiceImpl.getTransactionsSeller("Bearer eyJ0eXAiOiJKV1", true);
        assertEquals(1, actualTransactionsSeller.size());
        TransactionDto getResult = actualTransactionsSeller.get(0);
        assertEquals(1L, getResult.getAdvertisementId().longValue());
        assertEquals(2L, getResult.getUserId().longValue());
        assertEquals(1L, getResult.getId());
        assertEquals("0001-01-01", getResult.getDate().toLocalDate().toString());
        verify(transactionJpaRepository).getByAdvertisement_User_IdAndCustomerFlag((Long) any(), (Boolean) any());
        verify(tokenExtractData).extractUserIdFromToken((String) any());
    }

    @Test
    void testSetTransactionRating() {
        when(transactionJpaRepository.getReferenceById(anyLong())).thenReturn(getTransaction());
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        assertThrows(TokenCompareException.class,
                () -> transactionServiceImpl.setTransactionRating("Bearer eyJ0eXAiOiJKV1", 1L, 10));
        verify(transactionJpaRepository).getReferenceById(anyLong());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
    }
}

