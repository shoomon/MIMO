package com.bisang.backend.board.service;

import java.util.ArrayList;
import java.util.List;

import com.bisang.backend.board.controller.dto.BoardFileDto;
import com.bisang.backend.board.controller.dto.CommentDto;
import com.bisang.backend.board.controller.dto.SimpleBoardListDto;
import com.bisang.backend.board.controller.response.BoardListResponse;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.team.annotation.TeamMember;
import jakarta.persistence.EntityNotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.bisang.backend.board.controller.dto.BoardDto;
import com.bisang.backend.board.domain.*;
import com.bisang.backend.board.repository.*;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;
import com.bisang.backend.board.controller.response.BoardDetailResponse;

import lombok.RequiredArgsConstructor;

//todo: 게시글 리스트, 댓글 리스트 가져올 때 offset 설정
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
    private final CommentQuerydslRepository commentQuerydslRepository;
    private final S3Service s3Service;

//    @TeamMember
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
//        Long teamUserId = Long.parseLong(1+""); //테스트용
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
    
//    @TeamMember
    public BoardListResponse getPostList(Long teamBoardId){
        List<SimpleBoardListDto> list = boardQuerydslRepository.getBoardList(teamBoardId);
        return new BoardListResponse(list);
    }

//    @TeamMember
    public BoardDetailResponse getPostDetail(Long postId) {
        BoardDetailResponse postDetail = null;
        //조회수 증가
        boardJpaRepository.increaseViewCount(postId);
        //게시글 본문 정보, 댓글 정보 가져오기
        BoardDto post = boardQuerydslRepository.getBoardDetail(postId);
        List<CommentDto> comments = commentQuerydslRepository.getCommentList(postId);
        List<BoardFileDto> files = boardImageJpaRepository.findByBoardId(postId);

        postDetail = new BoardDetailResponse(post, files, comments);
        return postDetail;
    }

//    @TeamMember
    public void updatePost(Long userId, Long postId, String title, String description, List<BoardFileDto> filesToDelete,  List<BoardFileDto> filesToAdd) {
        Board post = boardJpaRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.updateTitle(title);
        boardJpaRepository.save(post);

        BoardDescription boardDescription = boardDescriptionJpaRepository.findById(post.getDescription().getId())
                .orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        boardDescription.updateDescription(description);
        boardDescriptionJpaRepository.save(boardDescription);
        //S3에서 파일 삭제
        for (BoardFileDto file : filesToDelete) {
            s3Service.deleteFile(userId, file.fileUri()); // S3에서 이미지 삭제
        }
        //파일 삭제
        for(BoardFileDto file : filesToDelete){
            BoardImage curfile = boardImageJpaRepository.findById(file.fileId())
                    .orElseThrow(()-> new EntityNotFoundException("첨부파일을 찾을 수 없습니다."));

            boardImageJpaRepository.delete(curfile);
        }
        //파일 추가
        for(BoardFileDto file : filesToAdd){
            String uri = file.fileUri();
            String fileExtension = uri.substring(uri.lastIndexOf(".") + 1).toLowerCase();

            boardImageJpaRepository.save(BoardImage.builder()
                    .boardId(post.getId())
                    .fileExtension(fileExtension)
                    .fileUri(uri)
                    .build());
        }
    }

//    @TeamMember
    public void deletePost(Long userId, Long postId){
        Board post = boardJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        //좋아요 삭제
        boardLikeRepository.deleteByBoardId(postId);
        //S3에서 이미지 삭제
//        List<BoardFileDto> boardFiles = boardImageJpaRepository.findByBoardId(postId);
//        for (BoardFileDto file : boardFiles) {
//            s3Service.deleteFile(userId, file.fileUri()); // S3에서 이미지 삭제
//        }
        //DB에서 파일 삭제
        boardImageJpaRepository.deleteByBoardId(postId);
        //게시글 설명 삭제
        boardDescriptionJpaRepository.deleteById(post.getDescription().getId());
        //게시글 삭제
        boardJpaRepository.delete(post);
        //댓글 삭제
        commentJpaReporitory.deleteByBoardId(postId);
    }
//todo: 좋아요 로직 작성
    public void likePost(Long userId, Long postId){

    }
}
