package com.senla.kanapa.service.mapper;

import com.senla.kanapa.entity.Message;
import com.senla.kanapa.service.dto.MessageDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageMapper {

    public MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .messageId(message.getId())
                .senderId(message.getSender().getId())
                .recipientId(message.getRecipient().getId())
                .date(message.getDate())
                .message(message.getMessageText())
                .build();
    }
}
