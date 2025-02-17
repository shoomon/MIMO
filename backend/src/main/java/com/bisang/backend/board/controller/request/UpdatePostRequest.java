package com.bisang.backend.board.controller.request;

import java.util.Collections;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.board.controller.dto.BoardFileDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public record UpdatePostRequest(
        @NotBlank(message = "제목을 입력하세요.")
        String title,
        @NotBlank(message = "내용을 입력하세요.")
        String description,
        String filesToDelete,
        MultipartFile[] filesToAdd
) {
    public List<BoardFileDto> getFilesToDeleteList() {
        if (filesToDelete == null || filesToDelete.isBlank()) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(filesToDelete, new TypeReference<List<BoardFileDto>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format for filesToDelete", e);
        }
    }
}
