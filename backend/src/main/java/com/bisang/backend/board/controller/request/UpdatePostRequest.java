package com.bisang.backend.board.controller.request;

import java.util.Collections;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.board.controller.dto.BoardFileDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public record UpdatePostRequest(
        String title,
        String description,
        String filesToDeleteJson,
        MultipartFile[] filesToAdd
) {
    public List<BoardFileDto> getFilesToDelete() {
        if (filesToDeleteJson == null || filesToDeleteJson.isBlank()) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(filesToDeleteJson, new TypeReference<List<BoardFileDto>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format for filesToDelete", e);
        }
    }
}
