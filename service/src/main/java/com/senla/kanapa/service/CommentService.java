package com.senla.kanapa.service;

import com.senla.kanapa.service.dto.CommentDto;
import com.senla.kanapa.service.exception.TokenCompareException;

import java.util.List;

public interface CommentService {

    void addComment(CommentDto commentDto, String token);

    void delComment(Long commentId, String token) throws TokenCompareException;

    List<CommentDto> getCommentsByAdvertisementId(Long advertisementId);

    void editComment(CommentDto commentDto, Long commentId, String token) throws TokenCompareException;
}
