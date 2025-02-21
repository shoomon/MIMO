package com.bisang.backend.user.service;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.s3.service.S3Service.CAT_IMAGE_URI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bisang.backend.board.controller.dto.BoardTeamDto;
import com.bisang.backend.board.controller.dto.SimpleBoardDto;
import com.bisang.backend.board.controller.dto.SimpleCommentDto;
import com.bisang.backend.board.domain.Comment;
import com.bisang.backend.board.repository.BoardJpaRepository;
import com.bisang.backend.board.repository.BoardQuerydslRepository;
import com.bisang.backend.board.repository.CommentJpaRepository;
import com.bisang.backend.common.exception.UserException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.s3.repository.ProfileImageRepository;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.user.controller.response.UserMyResponse;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final S3Service s3Service;
    private final ProfileImageRepository profileImageRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardQuerydslRepository boardQuerydslRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final CommentJpaRepository commentJpaRepository;

    @Transactional
    public void saveUser(User user) {
        userJpaRepository.save(user);
    }

    @Transactional
    public void updateUserInfo(User user, String nickname, String name, String profileUri) {
        try {
            user.updateNickname(nickname);
            user.updateName(name);
            if (profileUri != null) {
                user.updateProfileUri(profileUri);
            }
            userJpaRepository.save(user);
        } catch (UserException e) {
            if (profileUri != null) {
                s3Service.deleteFile(profileUri);
            }
            throw new UserException(e.getCode(), e.getMessage());
        } catch (Exception e) {
            if (profileUri != null) {
                s3Service.deleteFile(profileUri);
            }
            throw new UserException(INVALID_REQUEST);
        }
    }

    /**
     * 뒤에 기능들이 추가되면 추후 변경 필요
     */
    @Transactional(readOnly = true)
    public UserMyResponse getMyInfo(User user) {
        return UserMyResponse.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileUri(user.getProfileUri())
                .accountNumber(user.getAccountNumber())
                .mileage(0L)
                .mileageIncome(0L)
                .mileageOutcome(0L)
                .reviewScore(0D)
                .userBoard(getUserBoardList(user.getId()))
                .userComment(getUserCommentList(user.getId()))
                .build();
    }

//todo: 게시글+게시판+팀 테이블 join해서 가져오는 거랑 따로 조회해오는 거랑 어떤 게 성능이 더 좋을까
    public List<SimpleBoardDto> getUserBoardList(Long userId) {
        List<SimpleBoardDto> result = new ArrayList<>();
        //게시글+게시판+팀 정보
        Map<Long, BoardTeamDto> boardTeam = boardQuerydslRepository.getBoardTeamListByUserId(userId);
        System.out.println("게시글+게시판+팀: "+boardTeam.size());

        if(boardTeam.isEmpty()){
            return result;
        }

        List<Long> boardId = new ArrayList<>(boardTeam.keySet());

        Map<Long, String> thumbnail = boardQuerydslRepository.getImageThumbnailList(boardId);

        for(Long i : boardId) {
            BoardTeamDto boardTeamInfo = boardTeam.get(i);
            String imageUri = thumbnail.get(i);

            if(imageUri == null){
                imageUri = CAT_IMAGE_URI;
            }

            result.add(new SimpleBoardDto(boardTeamInfo, imageUri));
        }
        System.out.println("결과: "+result.size());
        return result;
    }

    public List<SimpleCommentDto> getUserCommentList(Long userId) {
        List<SimpleCommentDto> result = new ArrayList<>();

        List<Comment> commentList = commentJpaRepository.findAllByUserId(userId);
        System.out.println("댓글: "+commentList.size());

        if(commentList.isEmpty()) {
            return result;
        }
        //키 값은 commentId
        Map<Long, Comment> comment = commentList.stream()
                .collect(Collectors.toMap(Comment::getId, c -> c));
        //댓글 키 - 게시물 키 매핑
        Map<Long, Long> commentToBoardMap = commentList.stream()
                .collect(Collectors.toMap(Comment::getId, Comment::getBoardId));
        //댓글이 가진 게시글 키 리스트
        List<Long> commentBoardId = new ArrayList<>(commentToBoardMap.values());
        //키 값은 boardId
        Map<Long, BoardTeamDto> boardTeam = boardQuerydslRepository.getBoardTeamListByCommentId(commentBoardId);
        System.out.println("게시글+게시판+팀: "+boardTeam.size());
        //키 값은 boardId
        List<Long> boardId = new ArrayList<>(boardTeam.keySet());
        Map<Long, String> thumbnail = boardQuerydslRepository.getImageThumbnailList(boardId);


        for(Long i : comment.keySet()) {
            Comment commentInfo = comment.get(i);
            Long curBoardId = commentToBoardMap.get(i);

            BoardTeamDto boardTeamInfo = boardTeam.get(curBoardId);
            String imageUri = thumbnail.get(curBoardId);

            if(imageUri == null){
                imageUri = CAT_IMAGE_URI;
            }

            result.add(
                    new SimpleCommentDto(
                            boardTeamInfo,
                            commentInfo.getContent(),
                            commentInfo.getCreatedAt(),
                            imageUri)
            );
        }
        System.out.println("결과: "+result.size());

        return result;
    }

    public Optional<User> findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }
}
