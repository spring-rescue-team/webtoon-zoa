package com.project.webtoonzoa.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.project.webtoonzoa.dto.response.CommentDetailResponseDto;
import com.project.webtoonzoa.dto.response.CommentResponseDto;
import com.project.webtoonzoa.entity.Comment;
import com.project.webtoonzoa.repository.CommentRepository;
import com.project.webtoonzoa.repository.WebtoonRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest implements CommentTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    WebtoonRepository webtoonRepository;

    @BeforeAll
    static void commentTestData() {
    }

    @Test
    @DisplayName("댓글 생성")
    void createComment() {
        //given
        given(webtoonRepository.findById(TEST_WEBTOON_ID)).willReturn(Optional.of(TEST_WEBTOON));
        given(commentRepository.save(any(Comment.class))).willReturn(TEST_COMMENT);

        //when
        CommentResponseDto responseDto = commentService.createComment(TEST_USER, TEST_WEBTOON_ID, TEST_COMMENT_REQUEST_DTO);
        //then
        assertThat(responseDto.getId()).isEqualTo(TEST_COMMENT_ID);
    }

    @Test
    @DisplayName("댓글 조회")
    void readComment() {
        //given
        given(webtoonRepository.findById(TEST_WEBTOON_ID)).willReturn(Optional.of(TEST_WEBTOON));
        given(commentRepository.findByWebtoonId(TEST_WEBTOON_ID)).willReturn(List.of(TEST_COMMENT, TEST_COMMENT_2));

        //when
        List<CommentDetailResponseDto> responseDtos = commentService.readComment(TEST_WEBTOON_ID);

        //then
        assertThat(responseDtos.size()).isEqualTo(2);
        assertThat(responseDtos.get(0).getContent()).isEqualTo(TEST_COMMENT_CONTENT);
        assertThat(responseDtos.get(1).getContent()).isEqualTo(TEST_COMMENT_CONTENT_2);
    }
}