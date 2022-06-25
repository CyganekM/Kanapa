package com.senla.kanapa.controller;

import com.senla.kanapa.service.CommentService;
import com.senla.kanapa.service.dto.CommentDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Добавить комментарий к объявлению")
    @ResponseStatus(HttpStatus.OK)
    public void addComment(@RequestBody CommentDto commentDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        commentService.addComment(commentDto, token);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Удалить коментарий")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable Long commentId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws TokenCompareException {
        commentService.delComment(commentId, token);
    }

    @GetMapping("/advertisement/{advertisementId}")
    @Operation(summary = "Показать все коментари к данному объявлению")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentsByAdvertisementId(@PathVariable Long advertisementId) {
        return commentService.getCommentsByAdvertisementId(advertisementId);
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Редактировать комментарий")
    @ResponseStatus(HttpStatus.OK)
    public void editComments(@RequestBody CommentDto commentDto, @PathVariable Long commentId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws TokenCompareException {
        commentService.editComment(commentDto, commentId, token);
    }
}
