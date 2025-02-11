package com.bisang.backend.board.service;

import com.bisang.backend.board.controller.dto.CommentDto;
import com.bisang.backend.board.controller.dto.CommentListDto;
import com.bisang.backend.board.repository.CommentQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    CommentQuerydslRepository commentQuerydslRepository;

    public Map<CommentDto, List<CommentDto>> getCommentList(Long postId){
        Map<CommentDto, List<CommentDto>> result = new HashMap<>();
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
                result.put(comment, commentList.get(comment.commentId()));
            }
        }
        return result;
    }
}
