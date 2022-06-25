package com.senla.kanapa.service.impl;

import com.senla.kanapa.entity.Advertisement;
import com.senla.kanapa.entity.Comment;
import com.senla.kanapa.entity.User;
import com.senla.kanapa.repository.AdvertisementJpaRepository;
import com.senla.kanapa.repository.CommentJpaRepository;
import com.senla.kanapa.repository.UserJpaRepository;
import com.senla.kanapa.service.CommentService;
import com.senla.kanapa.service.dto.CommentDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import com.senla.kanapa.service.mapper.CommentMapper;
import com.senla.kanapa.service.security.TokenExtractData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentJpaRepository commentJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final AdvertisementJpaRepository advertisementJpaRepository;

    private final TokenExtractData tokenExtractData;

    @Override
    public void addComment(CommentDto commentDto, String token) {
        User user = userJpaRepository.getReferenceById(tokenExtractData.extractUserIdFromToken(token));
        Advertisement advertisement = advertisementJpaRepository.getReferenceById(commentDto.getAdvertisementId());
        Comment comment = new Comment(commentDto.getMessage(), LocalDateTime.now(), user, advertisement);
        commentJpaRepository.save(comment);
    }

    @Override
    public void delComment(Long commentId, String token) throws TokenCompareException {
        Comment comment = commentJpaRepository.getReferenceById(commentId);
        if (comment.getUser().getId().equals(tokenExtractData.extractUserIdFromToken(token))) {
            comment.setUser(null);
            comment.setAdvertisement(null);
            commentJpaRepository.delete(comment);
        } else {
            throw new TokenCompareException("You don't have access to delete this comment");
        }
    }

    @Override
    public List<CommentDto> getCommentsByAdvertisementId(Long advertisementId) {
        List<Comment> comments = commentJpaRepository.getByAdvertisement_Id(advertisementId);
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public void editComment(CommentDto commentDto, Long commentId, String token) throws TokenCompareException {
        Comment comment = commentJpaRepository.getReferenceById(commentId);
        if (comment.getUser().getId().equals(tokenExtractData.extractUserIdFromToken(token))) {
            comment.setMessage(commentDto.getMessage());
            commentJpaRepository.save(comment);
        } else {
            throw new TokenCompareException("You don't have access to edit this comment");
        }
    }
}
