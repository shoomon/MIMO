package com.bisang.backend.board.service;

import java.util.*;

import com.bisang.backend.board.controller.dto.*;
import com.bisang.backend.board.controller.response.BoardListResponse;
import com.bisang.backend.common.exception.BoardException;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.user.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.board.domain.*;
import com.bisang.backend.board.repository.*;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.board.controller.response.BoardDetailResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final TeamUserJpaRepository teamUserJpaRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final BoardImageJpaRepository boardImageJpaRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardDescriptionJpaRepository boardDescriptionJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final BoardQuerydslRepository boardQuerydslRepository;
    private final CommentQuerydslRepository commentQuerydslRepository;
    private final S3Service s3Service;
    private final UserJpaRepository userJpaRepository;
    private final TeamBoardJpaRepository teamBoardJpaRepository;

    @Transactional
    //    @TeamMember
    public Long createPost(
            Long teamBoardId,
            Long teamId,
            Long userId,
            String title,
            String description,
            MultipartFile[] files
    ) {
        TeamUser teamUser = teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new EntityNotFoundException("팀유저를 찾을 수 없습니다."));

        Long boardCount = boardJpaRepository.countBoardsByTeamBoardId(teamBoardId);

        if(boardCount > 10000){
            throw new BoardException(ExceptionCode.BOARD_LIMIT);
        }

        BoardDescription boardDescription = boardDescriptionJpaRepository.save(new BoardDescription(description));

        Board post = boardJpaRepository.save(Board.builder()
                .teamBoardId(teamBoardId)
                .teamUserId(teamUser.getId())
//                        .teamUserId(1L)
                .userId(userId)
                .title(title)
                .description(boardDescription)
                .build());
        //todo: 사진 업로드 비동기 처리
        if (files != null) {
            for(MultipartFile file : files){
//                if(file.isEmpty()) continue;
                String uri = s3Service.saveFile(userId, file);

                String fileExtension = uri.substring(uri.lastIndexOf(".") + 1).toLowerCase();

                boardImageJpaRepository.save(BoardImage.builder()
                        .boardId(post.getId())
                        .teamId(teamId)
                        .fileExtension(fileExtension)
                        .fileUri(uri)
                        .build());
            }
        }
        return post.getId();
    }

    public List<SimpleBoardListDto> getPostList(Long teamBoardId, Long offset, Integer pageSize) {
        List<SimpleBoardListDto> boardList = boardQuerydslRepository.getBoardList(teamBoardId, offset, pageSize);

        List<Long> boardIdList = boardList.stream().map(SimpleBoardListDto::getId).toList();

        Map<Long, Long> commentCount = boardQuerydslRepository.getCommentCount(boardIdList);
        Map<Long, String> imageUriList = boardQuerydslRepository.getImageThumbnailList(boardIdList);
        Map<Long, ProfileNicknameDto> userList = boardQuerydslRepository.getBoardUserList(boardIdList);

        List<SimpleBoardListDto> list = new ArrayList<>();

        for(SimpleBoardListDto board : boardList) {
            Long boardId = board.getId();

            Long comment = commentCount.getOrDefault(boardId, 0L);
            String imageUri = imageUriList.getOrDefault(boardId, "defaultImageUri");
            ProfileNicknameDto user = userList.getOrDefault(boardId, new ProfileNicknameDto(0L, "", ""));

            list.add(new SimpleBoardListDto(
                    board.getId(),
                    user.profileUri(),
                    user.nickname(),
                    board.postTitle(),
                    imageUri,
                    board.likeCount(),
                    board.viewCount(),
                    board.createdAt(),
                    board.updatedAt(),
                    comment
            ));
        }
        return list;
    }

    //    @TeamMember
    public BoardListResponse getPostListResponse(Long teamBoardId, Long offset, Integer pageSize){
        String teamBoardName = teamBoardJpaRepository.getTeamBoardNameById(teamBoardId);

        List<SimpleBoardListDto> list = getPostList(teamBoardId, offset, pageSize);

        return new BoardListResponse(teamBoardName, list);
    }

    @Transactional
    //    @TeamMember
    public BoardDetailResponse getPostDetail(Long postId) {
        BoardDetailResponse postDetail = null;
        boardJpaRepository.increaseViewCount(postId);

        BoardInfoDto postInfo = boardQuerydslRepository.getBoardInfo(postId);
        String userProfileUri = userJpaRepository.getUserProfileUri(postInfo.userId());
        String userNickname = teamUserJpaRepository.getTeamUserNickname(postInfo.teamUserId());
        BoardDto post = new BoardDto(
                postId,
                postInfo.userId(),
                postInfo.teamUserId(),
                userProfileUri,
                userNickname,
                postInfo.boardName(),
                postInfo.postTitle(),
                postInfo.description(),
                postInfo.likeCount(),
                postInfo.viewCount(),
                postInfo.createdAt(),
                postInfo.updatedAt()
        );

        List<CommentListDto> comments = getCommentList(postId);
        List<BoardFileDto> files = boardImageJpaRepository.findByBoardId(postId);

        postDetail = new BoardDetailResponse(post, files, comments);
        return postDetail;
    }

    @Transactional
    //    @TeamMember
    public void updatePost(
            Long userId,
            Long postId,
            String title,
            String description,
            List<BoardFileDto> filesToDelete,
            MultipartFile[] filesToAdd
    ) {
        Board post = boardJpaRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        TeamUser teamUser = teamUserJpaRepository.findById(post.getTeamUserId())
                .orElseThrow(() -> new EntityNotFoundException("팀유저를 찾을 수 없습니다."));

        if(!post.getUserId().equals(userId)) throw new BoardException(ExceptionCode.NOT_AUTHOR);

        post.updateTitle(title);
        boardJpaRepository.save(post);

        BoardDescription boardDescription = boardDescriptionJpaRepository.findById(post.getDescription().getId())
                .orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        boardDescription.updateDescription(description);
        boardDescriptionJpaRepository.save(boardDescription);
        System.out.println(filesToDelete.size());

        if(filesToDelete != null){
            for (BoardFileDto file : filesToDelete) {
                s3Service.deleteFile(file.fileUri()); // S3에서 이미지 삭제
            }
            for(BoardFileDto file : filesToDelete){
                BoardImage curfile = boardImageJpaRepository.findById(file.fileId())
                        .orElseThrow(()-> new EntityNotFoundException("첨부파일을 찾을 수 없습니다."));

                boardImageJpaRepository.delete(curfile);
            }
        }



        if(filesToAdd != null){
            for(MultipartFile file : filesToAdd){
                String uri = s3Service.saveFile(userId, file);
                String fileExtension = uri.substring(uri.lastIndexOf(".") + 1).toLowerCase();

                boardImageJpaRepository.save(BoardImage.builder()
                        .boardId(post.getId())
                        .teamId(teamUser.getTeamId())
//                                .teamId(1L)
                        .fileExtension(fileExtension)
                        .fileUri(uri)
                        .build());
            }
        }
    }

    @Transactional
    //    @TeamMember
    public void deletePost(Long userId, Long postId) {
        Board post = boardJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if(!post.getUserId().equals(userId)) throw new BoardException(ExceptionCode.NOT_AUTHOR);

        boardLikeRepository.deleteByBoardId(postId);

        List<BoardFileDto> boardFiles = boardImageJpaRepository.findByBoardId(postId);
        for (BoardFileDto file : boardFiles) {
            s3Service.deleteFile(file.fileUri()); // S3에서 이미지 삭제
        }

        boardImageJpaRepository.deleteByBoardId(postId);

        boardDescriptionJpaRepository.deleteById(post.getDescription().getId());

        boardJpaRepository.delete(post);

        commentJpaRepository.deleteByBoardId(postId);
    }

    @Transactional
    public void likePost(Long teamUserId, Long postId){
        Optional<BoardLike> userLike = boardLikeRepository.findByTeamUserIdAndBoardId(teamUserId, postId);

        if (userLike.isPresent()) {
            boardLikeRepository.delete(userLike.get());
            boardJpaRepository.decreaseLikeCount(postId);
        }else{
            boardLikeRepository.save(BoardLike.builder()
                    .teamUserId(teamUserId)
                    .boardId(postId)
                    .build()
            );
            boardJpaRepository.increaseLikeCount(postId);
        }
    }

    private List<CommentListDto> getCommentList(Long postId){
        List<CommentListDto> result = new ArrayList<>();
        Map<Long, List<CommentDto>> commentList = new HashMap<>();
        List<CommentDto> comments = commentQuerydslRepository.getCommentList(postId);

        for(CommentDto comment : comments) {
            if(comment.parentId() == null){
                if(!commentList.containsKey(comment.commentId())){
                    commentList.put(comment.commentId(), new ArrayList<>());
                }
            }else{
                if(!commentList.containsKey(comment.parentId())){
                    commentList.put(comment.parentId(), new ArrayList<>());
                }
                commentList.get(comment.parentId()).add(comment);
            }
        }
        for (CommentDto comment : comments) {
            if (comment.parentId() == null) { // 최상위 댓글
                result.add(new CommentListDto(comment, commentList.get(comment.commentId())));
            }
        }
        return result;
    }
}
