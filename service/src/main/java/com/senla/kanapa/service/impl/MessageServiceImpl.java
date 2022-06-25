package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Message;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.MessageJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.MessageService;
import com.senla.kanapa.service.dto.MessageDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import com.senla.kanapa.service.mapper.MessageMapper;
import com.senla.kanapa.service.security.TokenExtractData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageJpaRepository messageJpaRepository;
    private final TokenExtractData tokenExtractData;
    private final UserJpaRepository userJpaRepository;

    public void sendMessage(MessageDto messageDto, String token) {
        User sender = userJpaRepository.getReferenceById(tokenExtractData.extractUserIdFromToken(token));
        User recipient = userJpaRepository.getReferenceById(messageDto.getRecipientId());
        Message message = Message.builder()
                .sender(sender)
                .recipient(recipient)
                .messageText(messageDto.getMessage())
                .date(LocalDateTime.now())
                .build();
        messageJpaRepository.save(message);
    }

    @Override
    public void deleteMessage(Long messageId, String token) throws TokenCompareException {
        Message message = messageJpaRepository.getReferenceById(messageId);
        if (message.getSender().getId().equals(tokenExtractData.extractUserIdFromToken(token))) {
            message.setSender(null);
            message.setRecipient(null);
            messageJpaRepository.delete(message);
        } else {
            throw new TokenCompareException("You don't have access to delete this message");
        }
    }

    @Override
    public List<MessageDto> getTalk(Long recipientId, String token) {
        List<Message> messages = messageJpaRepository.getTalk(tokenExtractData.extractUserIdFromToken(token), recipientId);
        return messages.stream().map(MessageMapper::toMessageDto).collect(Collectors.toList());
    }

    @Override
    public void editMessage(MessageDto messageDto, Long messageId, String token) throws TokenCompareException {
        Message message = messageJpaRepository.getReferenceById(messageId);
        if (message.getSender().getId().equals(tokenExtractData.extractUserIdFromToken(token))) {
            message.setMessageText(messageDto.getMessage());
            messageJpaRepository.save(message);
        } else {
            throw new TokenCompareException("You don't have access to edit this message");
        }
    }
}

