package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.comment.CommentDetailResponseDto;
import com.project.webtoonzoa.dto.comment.CommentLikesResponseDto;
import com.project.webtoonzoa.dto.comment.CommentRequestDto;
import com.project.webtoonzoa.dto.comment.CommentResponseDto;
import com.project.webtoonzoa.entity.Comment;
import com.project.webtoonzoa.entity.CommentLikes;
import com.project.webtoonzoa.entity.Enum.UserRoleEnum;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.entity.Webtoon;
import com.project.webtoonzoa.repository.CommentLikesRepository;
import com.project.webtoonzoa.repository.CommentRepository;
import com.project.webtoonzoa.repository.UserRepository;
import com.project.webtoonzoa.repository.WebtoonRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikesRepository commentLikesRepository;
    private final WebtoonRepository webtoonRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto createComment(User user, Long webtoonId,
        CommentRequestDto requestDto) {
        Webtoon webtoon = checkExistWebtoon(webtoonId);
        Comment savedComment;
        if (requestDto.getParentId() != null) {
            Comment parentComment = checkExistComment(requestDto.getParentId());
            savedComment = commentRepository.save(
                new Comment(requestDto, user, webtoon, parentComment));
        } else {
            savedComment = commentRepository.save(new Comment(requestDto, user, webtoon));
        }
        return new CommentResponseDto(savedComment);
    }

    public List<CommentDetailResponseDto> readComment(Long webtoonId) {
        checkExistWebtoon(webtoonId);

        List<Comment> comments = commentRepository.findAllCommentByWebtoonId(webtoonId);

        List<CommentDetailResponseDto> commentResponseDTOList = new ArrayList<>();
        Map<Long, CommentDetailResponseDto> commentDTOHashMap = new HashMap<>();

        comments.forEach(c -> {
            CommentDetailResponseDto commentResponseDTO = new CommentDetailResponseDto(c);
            if (c.getDeletedAt() != null) {
                commentResponseDTO.setContent("삭제된 댓글입니다.");
            }
            commentDTOHashMap.put(commentResponseDTO.getId(), commentResponseDTO);
            if (c.getParent() != null) {
                commentDTOHashMap.get(c.getParent().getId()).getChildren().add(commentResponseDTO);
            } else {
                commentResponseDTOList.add(commentResponseDTO);
            }
        });
        return commentResponseDTOList;
    }

    @Transactional
    public CommentDetailResponseDto updateComment(User user, Long webtoonId, Long commentId,
        CommentRequestDto requestDto) {
        checkExistWebtoon(webtoonId);
        Comment comment = checkExistComment(commentId);
        validateUser(user, comment);
        comment.update(requestDto);
        return new CommentDetailResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto deleteComment(User user, Long webtoonId, Long commentId) {
        checkExistWebtoon(webtoonId);
        Comment comment = checkExistComment(commentId);
        if (!user.getRole().equals(UserRoleEnum.ADMIN)) {
            validateUser(user, comment);
        }
        comment.softDelete();
        return new CommentResponseDto(comment);
    }

    private Comment checkExistComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new NoSuchElementException("댓글이 존재하지 않습니다.")
        );
    }

    private CommentLikes checkExistCommentLikes(Long commentLikesId) {
        return commentLikesRepository.findById(commentLikesId).orElseThrow(
            () -> new NoSuchElementException("해당 댓글 좋아요가 존재하지 않습니다.")
        );
    }

    private Webtoon checkExistWebtoon(Long webtoonId) {
        return webtoonRepository.findById(webtoonId).orElseThrow(
            () -> new NoSuchElementException("웹툰이 존재하지 않습니다."));
    }

    private User checkExistUser(User user) {
        return userRepository.findById(user.getId()).orElseThrow(
            () -> new NoSuchElementException("유저가 존재하지 않습니다."));
    }

    private static void validateUser(User user, Comment comment) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("댓글 작성자만 수정, 삭제할 수 있습니다.");
        }
    }

    @Transactional
    public CommentLikesResponseDto createCommentLikes(User user, Long commentId) {
        User savedUser = checkExistUser(user);
        Comment savedComment = checkExistComment(commentId);
        if (commentLikesRepository.existsByUserAndComment(savedUser, savedComment)) {
            throw new DataIntegrityViolationException("이미 게시글에 좋아요를 했습니다.");
        }
        CommentLikes savedCommentLikes = commentLikesRepository.save(
            new CommentLikes(savedUser, savedComment));
        return new CommentLikesResponseDto(savedCommentLikes);
    }

    @Transactional
    public CommentLikesResponseDto deleteCommentLikes(User user, Long commentId) {
        User savedUser = checkExistUser(user);
        Comment savedComment = checkExistComment(commentId);
        CommentLikes savedCommentLikes = checkExistCommentLikes(commentId);
        if (!commentLikesRepository.existsByUserAndComment(savedUser, savedComment)) {
            throw new NoSuchElementException("해당 댓글 좋아요가 존재하지 않습니다.");
        }
        commentLikesRepository.delete(new CommentLikes(savedUser, savedComment));

        return new CommentLikesResponseDto(savedCommentLikes);
    }

}
