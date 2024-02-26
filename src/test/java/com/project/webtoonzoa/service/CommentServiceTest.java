package com.project.webtoonzoa.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.project.webtoonzoa.dto.response.CommentDetailResponseDto;
import com.project.webtoonzoa.dto.response.CommentResponseDto;
import com.project.webtoonzoa.entity.Comment;
import com.project.webtoonzoa.repository.CommentRepository;
import com.project.webtoonzoa.repository.WebtoonRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

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

    @Nested
    @DisplayName("댓글 수정")
    class updateComment {

        @Test
        @DisplayName("댓글 수정 성공")
        void updateComment_success() {
            //given
            given(webtoonRepository.findById(TEST_WEBTOON_ID)).willReturn(Optional.of(TEST_WEBTOON));
            given(commentRepository.findById(TEST_COMMENT_ID)).willReturn(Optional.of(TEST_COMMENT));

            //when
            CommentDetailResponseDto responseDto = commentService.updateComment(
                TEST_USER, TEST_WEBTOON_ID, TEST_COMMENT_ID, TEST_COMMENT_UPDATE_REQUEST_DTO);

            //then
            assertThat(responseDto.getUserId()).isEqualTo(TEST_USER_ID);
            assertThat(responseDto.getWebtoonId()).isEqualTo(TEST_WEBTOON_ID);
            assertThat(responseDto.getContent()).isEqualTo(TEST_COMMENT_UPDATE_CONTENT);
        }

        @Test
        @DisplayName("댓글 수정 실패_다른 유저")
        void updateComment_fail_anotherUser() {
            //given
            given(webtoonRepository.findById(TEST_WEBTOON_ID)).willReturn(Optional.of(TEST_WEBTOON));
            given(commentRepository.findById(TEST_COMMENT_ID)).willReturn(Optional.of(TEST_COMMENT));

            // when, then
            Assertions.assertThrows(AccessDeniedException.class, () ->
                commentService.updateComment(TEST_ANOTHER_USER, TEST_WEBTOON_ID, TEST_COMMENT_ID, TEST_COMMENT_UPDATE_REQUEST_DTO)
            );
        }
    }
}