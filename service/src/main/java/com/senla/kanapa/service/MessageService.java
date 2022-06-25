package com.senla.kanapa.service;

import com.senla.kanapa.service.dto.MessageDto;
import com.senla.kanapa.service.exception.TokenCompareException;

import java.util.List;

public interface MessageService {

    List<MessageDto> getTalk(Long recipientId, String token);

    void editMessage(MessageDto messageDto, Long messageId, String token) throws TokenCompareException;

    void sendMessage(MessageDto messageDto, String token);

    void deleteMessage(Long messageId, String token) throws TokenCompareException;
}
