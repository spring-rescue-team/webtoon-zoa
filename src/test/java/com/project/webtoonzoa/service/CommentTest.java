package com.project.webtoonzoa.service;

import com.project.webtoonzoa.dto.request.CommentRequestDto;
import com.project.webtoonzoa.entity.Comment;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.entity.Webtoon;
import org.springframework.test.util.ReflectionTestUtils;

public interface CommentTest {

    Long TEST_USER_ID = 1L;
    Long TEST_ANOTHER_USER_ID = 2L;
    User TEST_USER = User.builder()
        .id(TEST_USER_ID)
        .build();

    User TEST_ANOTHER_USER = User.builder()
        .id(TEST_ANOTHER_USER_ID)
        .build();

    Long TEST_WEBTOON_ID = 1L;
    Webtoon TEST_WEBTOON = Webtoon.builder()
        .id(TEST_WEBTOON_ID)
        .build();

    Long TEST_COMMENT_ID = 1L;
    Long TEST_COMMENT_ID2 = 2L;
    String TEST_COMMENT_CONTENT ="content";
    String TEST_COMMENT_CONTENT2 ="content2";


    Comment TEST_COMMENT = Comment.builder()
        .id(TEST_COMMENT_ID)
        .content(TEST_COMMENT_CONTENT)
        .user(TEST_USER)
        .webtoon(TEST_WEBTOON)
        .build();

    Comment TEST_COMMENT_2 = Comment.builder()
        .id(TEST_COMMENT_ID2)
        .content(TEST_COMMENT_CONTENT2)
        .user(TEST_ANOTHER_USER)
        .webtoon(TEST_WEBTOON)
        .build();

    CommentRequestDto TEST_COMMENT_REQUEST_DTO = CommentRequestDto.builder()
        .content(TEST_COMMENT_CONTENT)
        .build();
}
