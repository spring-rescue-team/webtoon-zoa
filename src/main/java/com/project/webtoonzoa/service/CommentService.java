package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.request.CommentRequestDto;
import com.project.webtoonzoa.dto.response.CommentDetailResponseDto;
import com.project.webtoonzoa.dto.response.CommentResponseDto;
import com.project.webtoonzoa.entity.Comment;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.entity.Webtoon;
import com.project.webtoonzoa.repository.CommentRepository;
import com.project.webtoonzoa.repository.WebtoonRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final WebtoonRepository webtoonRepository;

    @Transactional
    public CommentResponseDto createComment(User user, Long webtoonId, CommentRequestDto requestDto) {
        Webtoon webtoon = checkExistWebtoon(webtoonId);
        Comment savedComment = commentRepository.save(new Comment(requestDto, user, webtoon));
        return new CommentResponseDto(savedComment);
    }


    public List<CommentDetailResponseDto> readComment(Long webtoonId) {
        checkExistWebtoon(webtoonId);
        return commentRepository.findByWebtoonId(webtoonId)
            .stream()
            .map(comment -> new CommentDetailResponseDto(comment))
            .collect(Collectors.toList());
    }

    @Transactional
    public CommentDetailResponseDto updateComment(User user, Long webtoonId, Long commentId, CommentRequestDto requestDto) {
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
        validateUser(user, comment);
        comment.softDelete();
        return new CommentResponseDto(comment);
    }

    private Comment checkExistComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new NoSuchElementException("댓글이 존재하지 않습니다.")
        );
    }

    private Webtoon checkExistWebtoon(Long webtoonId) {
        return webtoonRepository.findById(webtoonId).orElseThrow(
            () -> new NoSuchElementException("웹툰이 존재하지 않습니다."));
    }

    private static void validateUser(User user, Comment comment) {
        if (!comment.getUser().equals(user)) {
            throw new AccessDeniedException("댓글 작성자만 수정할 수 있습니다.");
        }
    }
}
