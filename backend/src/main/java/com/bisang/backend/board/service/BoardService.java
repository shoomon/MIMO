package com.bisang.backend.board.service;

import com.bisang.backend.board.domain.*;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.bisang.backend.board.controller.response.BoardDetailResponse;
import com.bisang.backend.board.repository.BoardDescriptionJpaRepository;
import com.bisang.backend.board.repository.BoardImageJpaRepository;
import com.bisang.backend.board.repository.BoardJpaRepository;
import com.bisang.backend.board.repository.BoardLikeRepository;
import com.bisang.backend.board.repository.CommentJpaReporitory;
import com.bisang.backend.board.repository.TeamBoardJpaRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final UserJpaRepository userJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;
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
            List<String> fileUris
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

        if (!fileUris.isEmpty()) {
            for(String uri : fileUris){
                String fileExtension = uri.substring(uri.lastIndexOf(".") + 1).toLowerCase();

                boardImageJpaRepository.save(BoardImage.builder()
                        .boardId(post.getId())
                        .fileExtension(fileExtension)
                        .fileUri(uri)
                        .build());
            }

        }

    }

    public BoardDetailResponse getPost(Long postId) {

        Board post =  boardJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        TeamBoard boardType = teamBoardJpaRepository.findById(post.getTeamBoardId())
                .orElseThrow(() -> new EntityNotFoundException("게시판을 찾을 수 없습니다."));

        BoardDescription description = post.getDescription();

        LocalDateTime updatedAt = post.getCreatedAt() == post.getLastModifiedAt() ? post.getCreatedAt() : post.getLastModifiedAt();

        //todo: 댓글 엔티티 리스트 가져와서 댓글 DTO 리스트로 변환
        List<Comment> comments = commentJpaReporitory.findAllByBoardId(post.getId());
        for(Comment comment : comments){
            User user = userJpaRepository.findById(comment.getUserId())
                    .orElseThrow(()-> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
            String userProfileImage = user.getProfileUri();

            TeamUser teamUser = teamUserJpaRepository.findById(comment.getTeamUserId())
                    .orElseThrow(()-> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
            String nickname = user.getNickname();
        }
//                .stream()
//                .map(comment -> new CommentDto(comment.getUserId(), ))
//                .collect(Collectors.toList());
//
//        BoardDetailResponse boardDetailResponseDto = new BoardDetailResponse(
//                boardType.getBoardName(), post.getTitle(), description.getDescription(), comments, updatedAt
//        );
        return null;
    }
}
