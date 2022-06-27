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
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void sendMessage(MessageDto messageDto, String token) {
        User sender = userJpaRepository.getReferenceById(tokenExtractData.extractUserIdFromToken(token));
        log.info("The user {} sends a message", sender.getUsername());
        User recipient = userJpaRepository.getReferenceById(messageDto.getRecipientId());
        Message message = Message.builder()
                .sender(sender)
                .recipient(recipient)
                .messageText(messageDto.getMessage())
                .date(LocalDateTime.now())
                .build();
        messageJpaRepository.save(message);
        log.info("The message was sent successfully");
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId, String token) throws TokenCompareException {
        Long userId = tokenExtractData.extractUserIdFromToken(token);
        log.info("The user {} is deleting the message", userId);
        Message message = messageJpaRepository.getReferenceById(messageId);
        if (message.getSender().getId().equals(userId)) {
            message.setSender(null);
            message.setRecipient(null);
            messageJpaRepository.delete(message);
            log.info("Deleting of the message Id = {}  was successful", messageId);
        } else {
            throw new TokenCompareException("You don't have access to delete this message");
        }
    }

    @Override
    @Transactional
    public List<MessageDto> getTalk(Long recipientId, String token) {
        List<Message> messages = messageJpaRepository.getTalk(tokenExtractData.extractUserIdFromToken(token), recipientId);
        return messages.stream().map(MessageMapper::toMessageDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void editMessage(MessageDto messageDto, Long messageId, String token) throws TokenCompareException {
        Long userId = tokenExtractData.extractUserIdFromToken(token);
        log.info("The user {} is editing the message", userId);
        Message message = messageJpaRepository.getReferenceById(messageId);
        if (message.getSender().getId().equals(userId)) {
            message.setMessageText(messageDto.getMessage());
            messageJpaRepository.save(message);
            log.info("Editing of the message Id = {}  was successful", messageId);
        } else {
            throw new TokenCompareException("You don't have access to edit this message");
        }
    }
}

