package com.bisang.backend.board.service;

import java.util.*;

import com.bisang.backend.board.controller.dto.*;
import com.bisang.backend.board.controller.response.BoardListResponse;
import com.bisang.backend.common.exception.BoardException;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.team.annotation.TeamMember;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bisang.backend.board.domain.*;
import com.bisang.backend.board.repository.*;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.board.controller.response.BoardDetailResponse;

import lombok.RequiredArgsConstructor;

import static com.bisang.backend.common.exception.ExceptionCode.*;
import static com.bisang.backend.s3.service.S3Service.CAT_IMAGE_URI;

@Service
@RequiredArgsConstructor
public class BoardService {
    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

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
    private final TeamJpaRepository teamJpaRepository;

    @Transactional
    @TeamMember
    public Long createPost(
            Long userId,
            Long teamId,
            Long teamBoardId,
            String title,
            String description,
            MultipartFile[] files
    ) {
        TeamUser teamUser = teamUserJpaRepository.findByTeamIdAndUserId(teamId, userId)
                .orElseThrow(() -> new TeamException(NOT_FOUND_TEAM_USER));

        Long boardCount = boardJpaRepository.countBoardsByTeamBoardId(teamBoardId);

        if(boardCount > 10000){
            throw new BoardException(ExceptionCode.BOARD_LIMIT);
        }

        BoardDescription boardDescription = boardDescriptionJpaRepository.save(new BoardDescription(description));

        Board post = boardJpaRepository.save(Board.builder()
                .teamBoardId(teamBoardId)
                .teamId(teamId)
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
                        .teamBoardId(teamBoardId)
                        .boardId(post.getId())
                        .teamId(teamId)
                        .fileExtension(fileExtension)
                        .fileUri(uri)
                        .build());
            }
        }
        return post.getId();
    }

    public BoardListResponse getPostListResponse(User user, Long teamBoardId, Long offset, Integer pageSize){

        TeamBoard teamBoard = teamBoardJpaRepository.findTeamBoardById(teamBoardId)
                .orElseThrow(() -> new BoardException(TEAM_BOARD_NOT_FOUND));

        isValidGuest(user, teamBoard.getTeamId());

        List<SimpleBoardListDto> list = getPostList(teamBoardId, offset, pageSize);

        return new BoardListResponse(teamBoard.getBoardName(), list);
    }

    @Transactional
    public BoardDetailResponse getPostDetail(User user, Long boardId) {
        BoardDetailResponse postDetail = null;
        BoardInfoDto boardInfo = boardQuerydslRepository.getBoardInfo(boardId);

        TeamUser teamUser = isValidGuest(user, boardInfo.teamId());

        boardJpaRepository.increaseViewCount(boardId);

        String userProfileUri = userJpaRepository.getUserProfileUri(boardInfo.userId());
        String userNickname = teamUserJpaRepository.getTeamUserNickname(boardInfo.teamUserId());
        BoardLike userLike = null;
        if(teamUser != null){
            userLike = boardLikeRepository
                    .findByTeamUserIdAndBoardId(teamUser.getId(), boardId).orElse(null);
        }
        BoardDto post = new BoardDto(
                boardId,
                boardInfo.userId(),
                boardInfo.teamUserId(),
                userProfileUri,
                userNickname,
                boardInfo.boardName(),
                boardInfo.postTitle(),
                boardInfo.description(),
                boardInfo.likeCount(),
                boardInfo.viewCount(),
                boardInfo.createdAt(),
                boardInfo.updatedAt()
        );

        List<CommentListDto> comments = getCommentList(boardId);
        List<BoardFileDto> files = boardImageJpaRepository.findByBoardId(boardId);

        postDetail = new BoardDetailResponse(post, files, userLike != null, comments);
        return postDetail;
    }

    @Transactional
    public void updatePost(
            Long userId,
            Long boardId,
            String title,
            String description,
            List<BoardFileDto> filesToDelete,
            MultipartFile[] filesToAdd
    ) {
        Board board = boardJpaRepository.findById(boardId)
                .orElseThrow(()-> new BoardException(ExceptionCode.BOARD_NOT_FOUND));

        TeamUser teamUser = teamUserJpaRepository.findById(board.getTeamUserId())
                .orElseThrow(() -> new EntityNotFoundException("팀유저를 찾을 수 없습니다."));

        if(!board.getUserId().equals(userId)) throw new BoardException(ExceptionCode.NOT_AUTHOR);

        board.updateTitle(title);
        boardJpaRepository.save(board);

        BoardDescription boardDescription = boardDescriptionJpaRepository.findById(board.getDescription().getId())
                .orElseThrow(()-> new BoardException(ExceptionCode.BOARD_NOT_FOUND));

        boardDescription.updateDescription(description);
        boardDescriptionJpaRepository.save(boardDescription);

        if(filesToDelete != null){
            deleteImageFromS3(filesToDelete);
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
                        .teamBoardId(board.getTeamBoardId())
                        .boardId(board.getId())
                        .teamId(teamUser.getTeamId())
//                                .teamId(1L)
                        .fileExtension(fileExtension)
                        .fileUri(uri)
                        .build());
            }
        }
    }

    @Transactional
    public void deletePost(Long userId, Long boardId) {
        Board post = boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if(!post.getUserId().equals(userId)) throw new BoardException(ExceptionCode.NOT_AUTHOR);

        boardLikeRepository.deleteByBoardId(boardId);

        List<BoardFileDto> boardFiles = boardImageJpaRepository.findByBoardId(boardId);
        deleteImageFromS3(boardFiles);
        boardImageJpaRepository.deleteByBoardId(boardId);

        boardDescriptionJpaRepository.deleteById(post.getDescription().getId());

        boardJpaRepository.delete(post);

        commentJpaRepository.deleteByBoardId(boardId);
    }

    @Transactional
    public void likePost(Long teamUserId, Long postId){
        teamUserJpaRepository.findById(teamUserId)
                .orElseThrow(()-> new TeamException(NOT_FOUND_TEAM_USER));

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
            String imageUri = imageUriList.getOrDefault(boardId, CAT_IMAGE_URI);
            ProfileNicknameDto user = userList.getOrDefault(boardId, new ProfileNicknameDto(0L, "", ""));

            list.add(new SimpleBoardListDto(
                    board.getId(),
                    user.profileUri(),
                    user.nickname(),
                    board.boardTitle(),
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

    private TeamUser isValidGuest(User user, Long teamId) {
        Team team = teamJpaRepository.findTeamById(teamId)
                .orElseThrow(()->new TeamException(NOT_FOUND_TEAM));

        TeamUser teamUser = teamUserJpaRepository
                .findByTeamIdAndUserId(team.getId(), user.getId()).orElse(null);

        TeamPrivateStatus teamPrivateStatus = team.getPrivateStatus();

        if(TeamPrivateStatus.PRIVATE == teamPrivateStatus){
            if(user == null || teamUser == null){
                throw new TeamException(NOT_FOUND_TEAM_USER);
            }
        }
        return teamUser;
    }

    //todo: 로직이 뭔가 이상한디유
    // 방법 1. 자식이 있는 루트 댓글 soft delete -> 나중에 배치로 자식 없는데 안 지워진 댓글 지워줘야함 but 화면에 바로 반영 안됨
    // 방법 2. 댓글 조립하면서 부모 존재 여부 조회해보기
    private List<CommentListDto> getCommentList(Long postId){
        List<CommentListDto> result = new ArrayList<>();
        Map<Long, List<CommentDto>> commentList = new HashMap<>();
        Set<Long> addedKey = new HashSet<>();

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
                addedKey.add(comment.commentId());
            }else {
                if (addedKey.contains(comment.parentId())) continue;
                if(!commentList.containsKey(comment.parentId())){ //todo: 여기 지우면 대댓글 고쳐짐
                result.add(new CommentListDto(
                                new CommentDto(
                                        comment.parentId(),
                                        null,
                                        null,
                                        null,
                                        null,
                                        "삭제된 댓글입니다.",
                                        null,
                                        null
                                ),
                                commentList.get(comment.parentId())
                        )
                );
                addedKey.add(comment.parentId());
            } //todo: 여기도 지우기
            }
        }
        return result;
    }

    private void deleteImageFromS3(List<BoardFileDto> boardFiles) {
        List<BoardFileDto> deletedImage = new ArrayList<>();

        for(BoardFileDto image : boardFiles){
            try{
                s3Service.deleteFile(image.fileUri());
                deletedImage.add(image);
            }catch(Exception e){
                logger.error(e.getMessage());
                for(BoardFileDto deleted : deletedImage) {
                    boardImageJpaRepository.deleteById(deleted.fileId());
                }
            }
        }
    }
}
