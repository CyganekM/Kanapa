package com.senla.kanapa.controller;

import com.senla.kanapa.service.MessageService;
import com.senla.kanapa.service.dto.MessageDto;
import com.senla.kanapa.service.exception.TokenCompareException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/talk")
    @Operation(summary = "Показать диалог двух пользователей")
    public ResponseEntity<List<MessageDto>> getTalk(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam Long recipientId) {
        return ResponseEntity.ok().body(messageService.getTalk(recipientId, token));
    }

    @PostMapping
    @Operation(summary = "Отправить сообщение пользователю")
    @ResponseStatus(HttpStatus.OK)
    public void sendMessage(@RequestBody MessageDto messageDto, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        messageService.sendMessage(messageDto, token);
    }

    @PutMapping("/{messageId}")
    @Operation(summary = "Редактировать сообщение",
            description = "Редактировать сообщение может только его отправитель")
    @ResponseStatus(HttpStatus.OK)
    public void editMessage(@RequestBody MessageDto messageDto, @PathVariable Long messageId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws TokenCompareException {
        messageService.editMessage(messageDto, messageId, token);
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "Удалить сообщение")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMessage(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long messageId) throws TokenCompareException {
        messageService.deleteMessage(messageId, token);
    }
}
