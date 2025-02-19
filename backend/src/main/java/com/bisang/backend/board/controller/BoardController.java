package com.bisang.backend.board.controller;

import static com.bisang.backend.common.exception.ExceptionCode.PAGE_LIMIT;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.bisang.backend.auth.annotation.Guest;
import com.bisang.backend.board.domain.Board;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.board.controller.request.CreatePostRequest;
import com.bisang.backend.board.controller.request.LikePostRequest;
import com.bisang.backend.board.controller.request.UpdatePostRequest;
import com.bisang.backend.board.controller.response.BoardDetailResponse;
import com.bisang.backend.board.controller.response.BoardListResponse;
import com.bisang.backend.board.repository.BoardJpaRepository;
import com.bisang.backend.board.service.BoardService;
import com.bisang.backend.common.exception.BoardException;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

//todo: 권한 체크
@RestController
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPost(
            @AuthUser User user,
            @Valid @ModelAttribute CreatePostRequest request
    ) {
        return ResponseEntity.ok(
                boardService.createPost(
                        user.getId(),
                        request.teamId(),
                        request.teamBoardId(),
                        request.title(),
                        request.description(),
                        request.files()
                )
        );
    }

    @GetMapping("/list")
    public ResponseEntity<BoardListResponse> getPostList(
            @Guest User user,
            @RequestParam(value = "type", required = true) Long teamBoardId,
            @RequestParam(value = "page", required = true, defaultValue = "1") Long page
    ) {

        if(page > 1000) {
            throw new BoardException(PAGE_LIMIT);
        }

        Long offset = (page - 1) * SHORT_PAGE_SIZE;
        return ResponseEntity.ok(boardService.getPostListResponse(user, teamBoardId, offset, SHORT_PAGE_SIZE));
    }

//todo: 아래 테스트용 메소드 지우기
//    @GetMapping("/listAll")
//    public ResponseEntity<List<Board>> getPostListAll(@AuthUser User user) {
//        return ResponseEntity.ok(boardJpaRepository.findAll());
//    }

    @GetMapping("/detail")
    public ResponseEntity<BoardDetailResponse> getPostDetail(
            @Guest User user,
            @RequestParam(value = "post", required = true) Long postId
    ) {
        return ResponseEntity.ok(boardService.getPostDetail(user, postId));
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePost(
            @AuthUser User user,
            @RequestParam(value = "post", required = true) Long postId,
            @Valid @ModelAttribute UpdatePostRequest request
    ) {

        boardService.updatePost(
                user.getId(),
                postId,
                request.title(),
                request.description(),
                request.getFilesToDeleteList(),
                request.filesToAdd());
        return ResponseEntity.ok("게시글이 수정되었습니디.");
    }

    @DeleteMapping
    public ResponseEntity<String> deletePost(
            @AuthUser User user,
            @RequestParam(value = "post", required = true) Long postId
    ) {
        boardService.deletePost(user.getId(), postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    @PostMapping("/like")
    public ResponseEntity<Void> likePost(
            @AuthUser User user,
            @Valid @RequestBody LikePostRequest request
    ) {
        boardService.likePost(request.teamUserId(), request.boardId());
        return ResponseEntity.ok().build();
    }
}
