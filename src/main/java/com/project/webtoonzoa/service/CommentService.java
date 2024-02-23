package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.request.CommentRequestDto;
import com.project.webtoonzoa.entity.Comment;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.entity.Webtoon;
import com.project.webtoonzoa.repository.CommentRepository;
import com.project.webtoonzoa.repository.WebtoonRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final WebtoonRepository webtoonRepository;

    public void createComment(User user, CommentRequestDto requestDto) {
        Webtoon webtoon = webtoonRepository.findById(requestDto.getWebtoonId()).orElseThrow(
            () -> new NoSuchElementException()
        );
        commentRepository.save(new Comment(requestDto, user, webtoon));
    }
}
