package com.bisang.backend.board.service;

import com.bisang.backend.board.controller.dto.BoardDto;
import com.bisang.backend.board.domain.*;
import com.bisang.backend.board.repository.*;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.bisang.backend.board.controller.response.BoardDetailResponse;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final UserJpaRepository userJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final BoardImageJpaRepository boardImageJpaRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardDescriptionJpaRepository boardDescriptionJpaRepository;
    private final TeamBoardJpaRepository teamBoardJpaRepository;
    private final CommentJpaReporitory commentJpaReporitory;
    private final BoardQuerydslRepository boardQuerydslRepository;

    public void createPost(
            long teamBoardId,
            long teamId,
            long userId,
            String title,
            String description,
            List<String> fileUris
    ) {
        //게시글 본문 저장
        BoardDescription boardDescription = boardDescriptionJpaRepository.save(new BoardDescription(description));
        //팀유저 찾기
        TeamUser teamUser = teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new EntityNotFoundException("팀유저를 찾을 수 없습니다."));
        Long teamUserId = teamUser.getUserId();
//        Long teamUserId = Long.parseLong(1+"");
        //게시글 저장
        Board post = boardJpaRepository.save(Board.builder()
                .teamBoardId(teamBoardId)
                .teamUserId(teamUserId)
                .userId(userId)
                .title(title)
                .description(boardDescription)
                .build());
        //좋아요 저장
        boardLikeRepository.save(BoardLike.builder()
                                .boardId(post.getId())
                                .teamUserId(teamUserId)
                                .build());
        //파일은 게시글 저장 전 s3에 업로드
        //파일 uri가 있으면 저장
        if (fileUris != null && !fileUris.isEmpty()) {
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

        BoardDto post = boardQuerydslRepository.getBoardDetail(postId);

        //todo: 댓글 엔티티 리스트 가져와서 댓글 DTO 리스트로 변환
        List<Comment> comments = commentJpaReporitory.findAllByBoardId(post.postId());
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
