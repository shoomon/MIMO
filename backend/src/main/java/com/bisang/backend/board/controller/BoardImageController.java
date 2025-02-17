package com.bisang.backend.board.controller;

import static com.bisang.backend.common.utils.PageUtils.PAGE_SIZE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.board.controller.response.TeamAlbumResponse;
import com.bisang.backend.board.service.BoardImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/team-image")
public class BoardImageController {
    private final BoardImageService boardImageService;

    @GetMapping
    public ResponseEntity<TeamAlbumResponse> getAlbumImageList(
            @RequestParam(name = "team", required = true) Long teamId,
            @RequestParam(name = "last", required = false) Long lastReadImageId
    ) {
        return ResponseEntity.ok(
                boardImageService.getAlbumImages(teamId, lastReadImageId, PAGE_SIZE)
        );
    }
}
