package com.bisang.backend.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bisang.backend.board.controller.dto.*;
import com.bisang.backend.board.controller.response.BoardListResponse;
import com.bisang.backend.common.exception.BoardException;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.user.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.bisang.backend.board.domain.*;
import com.bisang.backend.board.repository.*;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.board.controller.response.BoardDetailResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

//todo: 게시글 리스트, 댓글 리스트 가져올 때 offset 설정
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

    //    @TeamMember
    public Long createPost(
            long teamBoardId,
            long teamUserId,
            long userId,
            String title,
            String description,
            List<MultipartFile> fileUris
    ) {
        BoardDescription boardDescription = boardDescriptionJpaRepository.save(new BoardDescription(description));
        validationTeamUser(teamUserId);

        Board post = boardJpaRepository.save(Board.builder()
                .teamBoardId(teamBoardId)
                .teamUserId(teamUserId)
                .userId(userId)
                .title(title)
                .description(boardDescription)
                .build());
        if (fileUris != null && !fileUris.isEmpty()) {
            for(MultipartFile file : fileUris){
                String uri = s3Service.saveFile(userId, file); //서비스가 서비스에 의존해도 되나 컨트롤러에서 업로드하고 file uri 리스트로 보내줘야하나

                String fileExtension = uri.substring(uri.lastIndexOf(".") + 1).toLowerCase();

                boardImageJpaRepository.save(BoardImage.builder()
                        .boardId(post.getId())
                        .fileExtension(fileExtension)
                        .fileUri(uri)
                        .build());
            }
        }
        return post.getId();
    }

    //    @TeamMember
    public BoardListResponse getPostList(Long teamBoardId, Long offset, Integer pageSize){
        String boardName = teamBoardJpaRepository.getTeamBoardNameById(teamBoardId);
        List<SimpleBoardListDto> list = boardQuerydslRepository.getBoardList(teamBoardId, offset, pageSize);
        return new BoardListResponse(boardName, list);
    }

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

//    @TeamMember
    public void updatePost(
            Long userId,
            Long postId,
            String title,
            String description,
            List<BoardFileDto> filesToDelete,
            List<MultipartFile> filesToAdd
    ) {
        Board post = boardJpaRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        if(!post.getUserId().equals(userId)) throw new BoardException(ExceptionCode.NOT_AUTHOR);

        if(title != null && !"".equals(title)){
            post.updateTitle(title);
            boardJpaRepository.save(post);
        }

        if(description != null && !"".equals(description)){
            BoardDescription boardDescription = boardDescriptionJpaRepository.findById(post.getDescription().getId())
                    .orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

            boardDescription.updateDescription(description);
            boardDescriptionJpaRepository.save(boardDescription);
        }

        if(filesToDelete != null){
            for (BoardFileDto file : filesToDelete) {
                s3Service.deleteFile(userId, file.fileUri()); // S3에서 이미지 삭제
            }
            for(BoardFileDto file : filesToDelete){
                BoardImage curfile = boardImageJpaRepository.findById(file.fileId())
                        .orElseThrow(()-> new EntityNotFoundException("첨부파일을 찾을 수 없습니다."));
        boardDescription.updateDescription(description);
        boardDescriptionJpaRepository.save(boardDescription);
        //S3에서 파일 삭제
        for (BoardFileDto file : filesToDelete) {
            s3Service.deleteFile(file.fileUri()); // S3에서 이미지 삭제
        }
        //파일 삭제
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
                System.out.println(uri);

                boardImageJpaRepository.save(BoardImage.builder()
                        .boardId(post.getId())
                        .fileExtension(fileExtension)
                        .fileUri(uri)
                        .build());
            }
        }
    }

//    @TeamMember
    public void deletePost(Long userId, Long postId){
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

    public String likePost(Long teamUserId, Long postId){
        Optional<BoardLike> userLike = boardLikeRepository.findByTeamUserIdAndBoardId(teamUserId, postId);
        System.out.println("유저 좋아요 결과: " + userLike.isPresent());

        if (userLike.isPresent()) {
            boardLikeRepository.delete(userLike.get());
            boardJpaRepository.decreaseLikeCount(postId);
            return "좋아요 감소";
        }else{
            boardLikeRepository.save(BoardLike.builder()
                            .teamUserId(teamUserId)
                            .boardId(postId)
                            .build()
                    );
            boardJpaRepository.increaseLikeCount(postId);
            return "좋아요 증가";
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

    private void validationTeamUser(long teamUserId) {
        teamUserJpaRepository.findById(teamUserId)
                .orElseThrow(() -> new EntityNotFoundException("팀유저를 찾을 수 없습니다."));
    }

}
