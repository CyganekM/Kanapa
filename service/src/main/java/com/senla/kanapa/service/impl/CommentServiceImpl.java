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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentJpaRepository commentJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final AdvertisementJpaRepository advertisementJpaRepository;

    private final TokenExtractData tokenExtractData;

    @Transactional
    @Override
    public void addComment(CommentDto commentDto, String token) {
        User user = userJpaRepository.getReferenceById(tokenExtractData.extractUserIdFromToken(token));
        log.info("The user {} started adding the comment", user.getUsername());
        Advertisement advertisement = advertisementJpaRepository.getReferenceById(commentDto.getAdvertisementId());
        Comment comment = new Comment(commentDto.getMessage(), LocalDateTime.now(), user, advertisement);
        commentJpaRepository.save(comment);
        log.info("The user {} has finished editing the comment", user.getUsername());
    }

    @Transactional
    @Override
    public void delComment(Long commentId, String token) throws TokenCompareException {
        Long userId = tokenExtractData.extractUserIdFromToken(token);
        log.info("The user {} started deleting the comment", userId);
        Comment comment = commentJpaRepository.getReferenceById(commentId);
        if (comment.getUser().getId().equals(userId)) {
            comment.setUser(null);
            comment.setAdvertisement(null);
            commentJpaRepository.delete(comment);
            log.info("The user Id = {} has finished deleting the comment Id = {}", userId, comment.getId());
        } else {
            log.error("The user Id ={} cannot edit the data", userId);
            throw new TokenCompareException("You don't have access to delete this comment");
        }
    }

    @Transactional
    @Override
    public List<CommentDto> getCommentsByAdvertisementId(Long advertisementId) {
        List<Comment> comments = commentJpaRepository.getByAdvertisement_Id(advertisementId);
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void editComment(CommentDto commentDto, Long commentId, String token) throws TokenCompareException {
        Long userId = tokenExtractData.extractUserIdFromToken(token);
        log.info("The user {} started editing the comment", userId);
        Comment comment = commentJpaRepository.getReferenceById(commentId);
        if (comment.getUser().getId().equals(userId)) {
            comment.setMessage(commentDto.getMessage());
            commentJpaRepository.save(comment);
            log.info("The user Id = {} has finished editing the comment Id = {}", userId, comment.getId());
        } else {
            log.error("The user Id ={} cannot edit the data", userId);
            throw new TokenCompareException("You don't have access to edit this comment");
        }
    }
}
