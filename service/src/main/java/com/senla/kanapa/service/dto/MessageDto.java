package com.senla.kanapa.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageDto {

    private Long messageId;
    private Long senderId;
    private Long recipientId;
    private LocalDateTime date;
    private String message;
}
