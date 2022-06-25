package com.senla.kanapa.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private Long commentId;
    private Long advertisementId;
    private Long userId;
    private String message;
    private LocalDateTime date;
}
