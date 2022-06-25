package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Category;
import com.senla.kanapa.entity.Comment;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.AdvertisementJpaRepository;
import com.senla.kanapa.repository.CommentJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.dto.CommentDto;
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
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CommentServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CommentServiceImplTest {
    @MockBean
    private AdvertisementJpaRepository advertisementJpaRepository;

    @MockBean
    private CommentJpaRepository commentJpaRepository;

    @Autowired
    private CommentServiceImpl commentServiceImpl;

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
        user.setId(1L);
        user.setKanapic(20);
        user.setName("John");
        user.setPassword("VeryVeryWellPassword123");
        user.setPhone("21528323423");
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
        user1.setPhone("2152234234");
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

    private List<Comment> getCommentList() {
        Comment comment = Comment.builder()
                .id(1L)
                .message("Not all who wander are lost")
                .user(getUserList().get(0))
                .date(LocalDateTime.now())
                .advertisement(getAdvertisementList().get(0))
                .build();
        Comment comment1 = Comment.builder()
                .id(2L)
                .message("All that glitters is not gold")
                .user(getUserList().get(1))
                .date(LocalDateTime.now())
                .advertisement(getAdvertisementList().get(0))
                .build();
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        commentList.add(comment1);
        return commentList;
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
    void CommentServiceImpl_addComment() {
        when(userJpaRepository.getReferenceById(1L)).thenReturn(getUserList().get(0));
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        when(commentJpaRepository.save(any())).thenReturn(getCommentList().get(1));
        when(advertisementJpaRepository.getReferenceById(anyLong())).thenReturn(getAdvertisementList().get(0));
        CommentDto commentDto = mock(CommentDto.class);
        when(commentDto.getAdvertisementId()).thenReturn(1L);
        when(commentDto.getMessage()).thenReturn("Not all who wander are lost");
        commentServiceImpl.addComment(commentDto, "chmxfgmfgkfgkgfhkcgh");
        verify(userJpaRepository).getReferenceById(anyLong());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(commentJpaRepository).save(any());
        verify(advertisementJpaRepository).getReferenceById(anyLong());
        verify(commentDto).getAdvertisementId();
        verify(commentDto).getMessage();
    }

    @Test
    void CommentServiceImpl_delComment_ExceptionCompareToken() {
        when(tokenExtractData.extractUserIdFromToken("Bearer eyJ0eXAiOiJKV1QiL")).thenReturn(1L);
        when(commentJpaRepository.getReferenceById(anyLong())).thenReturn(getCommentList().get(0));
        assertThrows(TokenCompareException.class, () -> commentServiceImpl.delComment(123L, "Bearer J0eXAiOiJKV1QiL"));
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(commentJpaRepository).getReferenceById(anyLong());
    }

    @Test
    void CommentServiceImpl_delComment() throws TokenCompareException {
        when(tokenExtractData.extractUserIdFromToken("Bearer eyJ0eXAiOiJKV1QiL")).thenReturn(1L);
        Comment comment = mock(Comment.class);
        when(comment.getUser()).thenReturn(getUserList().get(0));
        doNothing().when(comment).setAdvertisement(any());
        doNothing().when(comment).setDate(any());
        doNothing().when(comment).setId((anyLong()));
        doNothing().when(comment).setMessage(anyString());
        doNothing().when(comment).setUser((any()));
        comment.setAdvertisement(getAdvertisementList().get(0));
        comment.setDate(LocalDateTime.now());
        comment.setId(1L);
        comment.setMessage("I should have written something here");
        comment.setUser(getUserList().get(0));
        doNothing().when(commentJpaRepository).delete((Comment) any());
        when(commentJpaRepository.getReferenceById(anyLong())).thenReturn(comment);
        commentServiceImpl.delComment(1L, "Bearer eyJ0eXAiOiJKV1QiL");
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(commentJpaRepository).getReferenceById(anyLong());
        verify(commentJpaRepository).delete(any());
        verify(comment).getUser();
        verify(comment, atLeast(1)).setAdvertisement(any());
        verify(comment).setDate(any());
        verify(comment).setId(anyLong());
        verify(comment).setMessage(anyString());
        verify(comment, atLeast(1)).setUser((User) any());
    }

    @Test
    void CommentServiceImpl_getCommentsByAdvertisementId_ListEmpty() {
        when(commentJpaRepository.getByAdvertisement_Id(anyLong())).thenReturn(new ArrayList<>());
        assertTrue(commentServiceImpl.getCommentsByAdvertisementId(123L).isEmpty());
        verify(commentJpaRepository).getByAdvertisement_Id(anyLong());
    }

    @Test
    void CommentServiceImpl_getCommentsByAdvertisementId() {
        when(commentJpaRepository.getByAdvertisement_Id(anyLong())).thenReturn(getCommentList());
        List<CommentDto> actualCommentsByAdvertisementId = commentServiceImpl.getCommentsByAdvertisementId(123L);
        assertEquals(2, actualCommentsByAdvertisementId.size());
        CommentDto getResult = actualCommentsByAdvertisementId.get(0);
        assertEquals(1L, getResult.getAdvertisementId());
        assertEquals(1L, getResult.getUserId().longValue());
        assertEquals("Not all who wander are lost", getResult.getMessage());
        assertEquals(1L, getResult.getCommentId().longValue());
        verify(commentJpaRepository).getByAdvertisement_Id(anyLong());
    }


    @Test
    void CommentServiceImpl_editComment_exceptionCompareToken() {
        when(tokenExtractData.extractUserIdFromToken((anyString()))).thenReturn(1L);
        when(commentJpaRepository.getReferenceById((anyLong()))).thenReturn(getCommentList().get(1));
        CommentDto commentDto = mock(CommentDto.class);
        when(commentDto.getCommentId()).thenReturn(1L);
        assertThrows(TokenCompareException.class, () -> commentServiceImpl.editComment(commentDto, 1L, "dfsdfsdfsd"));
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(commentJpaRepository).getReferenceById(anyLong());
    }

    @Test
    void CommentServiceImpl_editComment() throws TokenCompareException {
        when(tokenExtractData.extractUserIdFromToken((String) any())).thenReturn(1L);

        Comment comment = mock(Comment.class);
        when(comment.getUser()).thenReturn(getUserList().get(0));
        doNothing().when(comment).setAdvertisement((Advertisement) any());
        doNothing().when(comment).setDate((LocalDateTime) any());
        doNothing().when(comment).setId((Long) any());
        doNothing().when(comment).setMessage((String) any());
        doNothing().when(comment).setUser((User) any());
        comment.setAdvertisement(getAdvertisementList().get(0));
        comment.setDate(LocalDateTime.of(1, 1, 1, 1, 1));
        comment.setId(123L);
        comment.setMessage("Not all who wander are lost");
        comment.setUser(getUserList().get(0));

        Comment comment1 = new Comment();
        comment1.setAdvertisement(getAdvertisementList().get(0));
        comment1.setDate(LocalDateTime.of(1, 1, 1, 1, 1));
        comment1.setId(123L);
        comment1.setMessage("Not all who wander are lost");
        comment1.setUser(getUserList().get(0));
        when(commentJpaRepository.save(any())).thenReturn(comment1);
        when(commentJpaRepository.getReferenceById((Long) any())).thenReturn(comment);
        CommentDto commentDto = mock(CommentDto.class);
        when(commentDto.getMessage()).thenReturn("Not all who wander are lost");
        when(commentDto.getCommentId()).thenReturn(123L);
        commentServiceImpl.editComment(commentDto, 1L, "ABC123");
        verify(tokenExtractData).extractUserIdFromToken(any());
        verify(commentJpaRepository).getReferenceById(anyLong());
        verify(commentJpaRepository).save((Comment) any());
        verify(comment).getUser();
        verify(comment).setAdvertisement(any());
        verify(comment).setDate(any());
        verify(comment).setId(anyLong());
        verify(comment, atLeast(1)).setMessage((String) any());
        verify(comment).setUser(any());
        verify(commentDto).getMessage();
    }
}

