package com.project.webtoonzoa.controller;

import com.project.webtoonzoa.dto.webtoon.WebtoonRequestDto;
import com.project.webtoonzoa.dto.webtoon.WebtoonResponseDto;
import com.project.webtoonzoa.global.response.CommonResponse;
import com.project.webtoonzoa.global.util.UserDetailsImpl;
import com.project.webtoonzoa.service.WebtoonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webtoons")
public class WebtoonController {

    private final WebtoonService webtoonService;

    @PostMapping
    public ResponseEntity<CommonResponse<WebtoonResponseDto>> createWebtoon(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody WebtoonRequestDto requestDto) {

        webtoonService.createWebtoon(userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<WebtoonResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("웹툰이 게시 되었습니다")
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<WebtoonResponseDto>>> findAllWebtoon() {
        List<WebtoonResponseDto> responseDtoList = webtoonService.findAllWebtoon();

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<List<WebtoonResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("웹툰 조회 성공")
                .data(responseDtoList)
                .build()
        );
    }


    @GetMapping("/{webtoonId}")
    public ResponseEntity<CommonResponse<WebtoonResponseDto>> readWebtoon(
        @PathVariable Long webtoonId) {

        WebtoonResponseDto webtoonResponseDto = webtoonService.readWebtoon(webtoonId);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<WebtoonResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("웹툰 상세정보 조회 성공")
                .data(webtoonResponseDto)
                .build()
        );
    }

    @PutMapping("/{webtoonId}")
    public ResponseEntity<CommonResponse<WebtoonResponseDto>> updateWebtoon(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long webtoonId,
        @RequestBody WebtoonRequestDto requestDto) {

        WebtoonResponseDto responseDto = webtoonService.updateWebtoon(userDetails.getUser(),
            webtoonId, requestDto);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<WebtoonResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("웹툰 수정 성공")
                .data(responseDto)
                .build()
        );
    }


    @DeleteMapping("/{webtoonId}")
    public ResponseEntity<CommonResponse<Long>> deleteWebtoon(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long webtoonId) {

        Long responseDto = webtoonService.deleteWebtoon(userDetails.getUser(),
            webtoonId);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<Long>builder()
                .status(HttpStatus.OK.value())
                .message("웹툰 삭제 성공")
                .data(responseDto)
                .build()
        );
    }

    @PostMapping("/{webtoonId}/likes")
    public ResponseEntity<CommonResponse<WebtoonLikesResponseDto>> createWebtoonLikes(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "webtoonId") Long webtoonId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.<WebtoonLikesResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("댓글 좋아요 성공")
                .data(webtoonService.createWebtoonLikes(new User(), webtoonId))
                .build()
        );
    }

    @DeleteMapping("/{webtoonId}/likes")
    public ResponseEntity<CommonResponse<WebtoonLikesResponseDto>> deleteWebtoonLikes(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "webtoonId") Long webtoonId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.<WebtoonLikesResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("댓글 좋아요 취소 성공")
                .data(webtoonService.deleteWebtoonLikes(new User(), webtoonId))
                .build()
        );
    }
}
