package com.senla.kanapa.service.mapper;

import com.senla.kanapa.entity.Comment;
import com.senla.kanapa.service.dto.CommentDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getId())
                .advertisementId(comment.getAdvertisement().getId())
                .userId(comment.getUser().getId())
                .date(comment.getDate())
                .message(comment.getMessage())
                .build();
    }
}
