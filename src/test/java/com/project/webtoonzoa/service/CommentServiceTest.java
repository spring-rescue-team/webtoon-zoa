package com.project.webtoonzoa.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.project.webtoonzoa.dto.request.CommentRequestDto;
import com.project.webtoonzoa.dto.response.CommentResponseDto;
import com.project.webtoonzoa.entity.Comment;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.entity.Webtoon;
import com.project.webtoonzoa.repository.CommentRepository;
import com.project.webtoonzoa.repository.WebtoonRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
        Assertions.assertThat(responseDto.getId()).isEqualTo(TEST_COMMENT_ID);
    }
}