package com.bisang.backend.board.controller;

import com.bisang.backend.common.exception.BoardException;
import com.bisang.backend.common.utils.PageUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bisang.backend.auth.annotation.AuthUser;
import com.bisang.backend.board.controller.request.CreatePostRequest;
import com.bisang.backend.board.controller.request.LikePostRequest;
import com.bisang.backend.board.controller.request.UpdatePostRequest;
import com.bisang.backend.board.controller.response.BoardDetailResponse;
import com.bisang.backend.board.controller.response.BoardListResponse;
import com.bisang.backend.board.repository.BoardJpaRepository;
import com.bisang.backend.board.service.BoardService;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.bisang.backend.common.exception.ExceptionCode.PAGE_LIMIT;
import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;

//todo: 권한 체크, 500 에러 말고 커스텀 exception 구현 필요
@RestController
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final S3Service s3Service;
    private final BoardService boardService;
    private final BoardJpaRepository boardJpaRepository;

    //todo: 게시글 생성 시 S3 업로드 및 사진 uri 리스트 저장
    @Transactional
    @PostMapping
    public ResponseEntity<Long> createPost(
            @AuthUser User user,
            @Valid @RequestBody CreatePostRequest request
    ) {
        return ResponseEntity.ok(
                boardService.createPost(
                        request.teamBoardId(),
                        request.teamUserId(),
                        user.getId(),
                        request.title(),
                        request.description(),
                        request.fileUris()
                )
        );
    }

    @GetMapping("/list")
    public ResponseEntity<BoardListResponse> getPostList(
            @AuthUser User user,
            @RequestParam(value = "type", required = true) Long teamBoardId,
            @RequestParam(value = "page", required = true) Long page
    ) {
        if(page > 1000) throw new BoardException(PAGE_LIMIT);

        Long offset = (page-1) * SHORT_PAGE_SIZE;
        return ResponseEntity.ok(boardService.getPostList(teamBoardId, offset, SHORT_PAGE_SIZE));
    }

//todo: 아래 테스트용 메소드 지우기
//    @GetMapping("/listAll")
//    public ResponseEntity<List<Board>> getPostListAll(@AuthUser User user) {
//        return ResponseEntity.ok(boardJpaRepository.findAll());
//    }

    @GetMapping("/detail")
    public ResponseEntity<BoardDetailResponse> getPostDetail(
            @AuthUser User user,
            @RequestParam(value = "post", required = true) Long postId
    ) {
        return ResponseEntity.ok(boardService.getPostDetail(postId));
    }

    @Transactional
    @PatchMapping
    public ResponseEntity<String> updatePost(
            @AuthUser User user,
            @RequestParam(value = "post", required = true) Long postId,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        boardService.updatePost(
                user.getId(),
                postId,
                request.title(),
                request.description(),
                request.filesToDelete(),
                request.filesToAdd());
        return ResponseEntity.ok("게시글이 수정되었습니디.");
    }

    @Transactional
    @DeleteMapping
    public ResponseEntity<String> deletePost(
            @AuthUser User user,
            @RequestParam(value = "post", required = true) Long postId
    ) {
        boardService.deletePost(user.getId(), postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    //todo: 좋아요 구현
    @Transactional
    @PostMapping("/like")
    public ResponseEntity<String> likePost(
            @AuthUser User user,
            @Valid @RequestBody LikePostRequest request
    ) {
        return ResponseEntity.ok(boardService.likePost(request.teamUserId(), request.boardId()));
    }

//    @PostMapping("/{id}")
//    public ResponseEntity<String> uploadImage(
//            @AuthSimpleUser SimpleUser user,
//            @PathVariable("id")Integer id,
//            @RequestPart("file") MultipartFile multipartFile
//    ) {
//        String returnUrl = s3Service.saveFile(user.userId(), multipartFile);
//
//        return ResponseEntity.ok(returnUrl);
//    }
}
