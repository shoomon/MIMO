package com.bisang.backend.board.service;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.bisang.backend.board.controller.response.BoardDetailResponse;
import com.bisang.backend.board.domain.Board;
import com.bisang.backend.board.domain.BoardDescription;
import com.bisang.backend.board.domain.BoardImage;
import com.bisang.backend.board.domain.BoardLike;
import com.bisang.backend.board.domain.TeamBoard;
import com.bisang.backend.board.repository.BoardDescriptionJpaRepository;
import com.bisang.backend.board.repository.BoardImageJpaRepository;
import com.bisang.backend.board.repository.BoardJpaRepository;
import com.bisang.backend.board.repository.BoardLikeRepository;
import com.bisang.backend.board.repository.CommentJpaReporitory;
import com.bisang.backend.board.repository.TeamBoardJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
    BoardJpaRepository boardJpaRepository;
    BoardImageJpaRepository boardImageJpaRepository;
    BoardLikeRepository boardLikeRepository;
    BoardDescriptionJpaRepository boardDescriptionJpaRepository;
    TeamBoardJpaRepository teamBoardJpaRepository;
    CommentJpaReporitory commentJpaReporitory;

    public void createPost(
            long teamBoardId,
            long teamUserId,
            long userId,
            String title,
            String description,
            String fileUri,
            String fileExtension
    ) {
        BoardDescription boardDescription = boardDescriptionJpaRepository.save(new BoardDescription(description));

        Board newPost = Board.builder()
                            .teamBoardId(teamBoardId)
                            .teamUserId(teamUserId)
                            .userId(userId)
                            .title(title)
                            .description(boardDescription)
                            .build();

        Board post = boardJpaRepository.save(newPost);
        boardLikeRepository.save(BoardLike.builder()
                                .boardId(post.getId())
                                .teamUserId(teamUserId)
                                .build());

        if (fileUri != null || !fileUri.isEmpty()) {
            boardImageJpaRepository.save(BoardImage.builder()
                    .boardId(post.getId())
                    .fileExtension(fileExtension)
                    .fileUri(fileUri)
                    .build());
        }

    }

    public BoardDetailResponse getPost(Long postId) {

        Board post =  boardJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        TeamBoard boardType = teamBoardJpaRepository.findById(post.getTeamBoardId())
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

        BoardDescription description = post.getDescription();

        //todo: 댓글 엔티티 리스트 가져와서 댓글 DTO 리스트로 변환
//        List<CommentDto> comments = commentJpaReporitory.findAllByBoardId(post.getId())
//                .stream()
//                .map(comment -> new CommentDto(comment.getUserId(), ))
//                .collect(Collectors.toList());
//
//        BoardDetailResponse boardDetailResponseDto = new BoardDetailResponse(
//                boardType.getBoardName(), post.getTitle(), description.getDescription(), comments
//        );
        return null;
    }
}
