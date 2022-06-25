package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Message;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.MessageJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.dto.MessageDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import com.senla.kanapa.service.security.TokenExtractData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

@ContextConfiguration(classes = {MessageServiceImpl.class})
@ExtendWith(SpringExtension.class)
class MessageServiceImplTest {
    @MockBean
    private MessageJpaRepository messageJpaRepository;

    @Autowired
    private MessageServiceImpl messageServiceImpl;

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

    private Message getMessage() {
        Message message = new Message();
        message.setDate(LocalDateTime.of(1, 1, 1, 1, 1));
        message.setId(1L);
        message.setMessageText("Not all who wander are lost");
        message.setRecipient(getUserList().get(0));
        message.setSender(getUserList().get(1));
        return message;
    }

    @Test
    void MessageServiceImpl_sendMessage() {
        when(userJpaRepository.getReferenceById(anyLong())).thenReturn(getUserList().get(0));
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        when(messageJpaRepository.save(any())).thenReturn(getMessage());
        MessageDto messageDto = mock(MessageDto.class);
        when(messageDto.getRecipientId()).thenReturn(1L);
        when(messageDto.getMessage()).thenReturn("Not all who wander are lost");
        messageServiceImpl.sendMessage(messageDto, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzZWN1cmUtYXBpIiwiYX");
        verify(userJpaRepository, atLeast(1)).getReferenceById(anyLong());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(messageJpaRepository).save(any());
        verify(messageDto).getRecipientId();
        verify(messageDto).getMessage();
    }

    @Test
    void MessageServiceImpl_deleteMessage_ExceptionCompareToken() {
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        when(messageJpaRepository.getReferenceById((anyLong()))).thenReturn(getMessage());
        assertThrows(TokenCompareException.class, () -> messageServiceImpl.deleteMessage(1L, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzZWN1cmUtYXBpIiwiYX"));
        verify(tokenExtractData).extractUserIdFromToken((String) any());
        verify(messageJpaRepository).getReferenceById((Long) any());
    }

    @Test
    void MessageServiceImpl_deleteMessage() {
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        Message message = mock(Message.class);
        when(message.getSender()).thenReturn(getUserList().get(0));
        doNothing().when(message).setDate(any());
        doNothing().when(message).setId(anyLong());
        doNothing().when(message).setMessageText(anyString());
        doNothing().when(message).setRecipient(any());
        doNothing().when(message).setSender(any());
        message.setDate(LocalDateTime.of(1, 1, 1, 1, 1));
        message.setId(123L);
        message.setMessageText("Not all who wander are lost");
        message.setRecipient(getUserList().get(1));
        message.setSender(getUserList().get(0));
        when(messageJpaRepository.getReferenceById(anyLong())).thenReturn(message);
        assertThrows(TokenCompareException.class, () -> messageServiceImpl.deleteMessage(1L, "Bearer eyJ0eXAi"));
        verify(tokenExtractData).extractUserIdFromToken((String) any());
        verify(messageJpaRepository).getReferenceById((Long) any());
        verify(message).getSender();
        verify(message).setDate((LocalDateTime) any());
        verify(message).setId(anyLong());
        verify(message).setMessageText(anyString());
        verify(message).setRecipient(any());
        verify(message).setSender(any());
    }

    @Test
    void MessageServiceImpl_getTalk_empty() {
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        when(messageJpaRepository.getTalk(anyLong(), anyLong())).thenReturn(new ArrayList<>());
        assertTrue(messageServiceImpl.getTalk(1L, "Bearer eyJ0eXAi").isEmpty());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(messageJpaRepository).getTalk(anyLong(), anyLong());
    }

    @Test
    void MessageServiceImpl_getTalk() {
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        ArrayList<Message> messageList = new ArrayList<>();
        messageList.add(getMessage());
        when(messageJpaRepository.getTalk(anyLong(), anyLong())).thenReturn(messageList);
        List<MessageDto> actualTalk = messageServiceImpl.getTalk(1L, "Bearer eyJ0eXAi");
        assertEquals(1, actualTalk.size());
        MessageDto getResult = actualTalk.get(0);
        assertEquals(10L, getResult.getSenderId().longValue());
        assertEquals("01:01", getResult.getDate().toLocalTime().toString());
        assertEquals(10L, getResult.getRecipientId().longValue());
        assertEquals(1L, getResult.getMessageId().longValue());
        assertEquals("Not all who wander are lost", getResult.getMessage());
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(messageJpaRepository).getTalk(anyLong(), anyLong());
    }

    @Test
    void MessageServiceImpl_editMessage_ExceptionCompareToken() {
        when(tokenExtractData.extractUserIdFromToken(anyString())).thenReturn(1L);
        when(messageJpaRepository.getReferenceById(anyLong())).thenReturn(getMessage());
        MessageDto messageDto = mock(MessageDto.class);
        when(messageDto.getMessageId()).thenReturn(1L);
        assertThrows(TokenCompareException.class, () -> messageServiceImpl.editMessage(messageDto, 1L, "Bearer eyJ0eXAi"));
        verify(tokenExtractData).extractUserIdFromToken(anyString());
        verify(messageJpaRepository).getReferenceById(anyLong());
    }

    @Test
    void MessageServiceImpl_editMessage() {
        when(tokenExtractData.extractUserIdFromToken((String) any())).thenReturn(1L);
        Message message = mock(Message.class);
        when(message.getSender()).thenReturn(getUserList().get(0));
        doNothing().when(message).setDate(any());
        doNothing().when(message).setId(anyLong());
        doNothing().when(message).setMessageText(anyString());
        doNothing().when(message).setRecipient(any());
        doNothing().when(message).setSender(any());
        message.setDate(LocalDateTime.of(1, 1, 1, 1, 1));
        message.setId(1L);
        message.setMessageText("Not all who wander are lost");
        message.setRecipient(getUserList().get(1));
        message.setSender(getUserList().get(0));
        when(messageJpaRepository.getReferenceById(anyLong())).thenReturn(message);
        MessageDto messageDto = mock(MessageDto.class);
        when(messageDto.getMessageId()).thenReturn(1L);
        assertThrows(TokenCompareException.class, () -> messageServiceImpl.editMessage(messageDto, 1L, "ABC123"));
        verify(tokenExtractData).extractUserIdFromToken((String) any());
        verify(messageJpaRepository).getReferenceById((Long) any());
        verify(message).getSender();
        verify(message).setDate((LocalDateTime) any());
        verify(message).setId(anyLong());
        verify(message).setMessageText(anyString());
        verify(message).setRecipient(any());
        verify(message).setSender(any());
    }
}

