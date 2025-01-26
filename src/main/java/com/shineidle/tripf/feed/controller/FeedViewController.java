package com.shineidle.tripf.feed.controller;

import com.shineidle.tripf.feed.dto.*;
import com.shineidle.tripf.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/feeds")
@Validated
public class FeedViewController {

    private final FeedService feedService;

    /**
     * 피드 작성 페이지를 렌더링합니다.
     *
     * @return 피드 작성 페이지의 뷰 이름
     */
    @GetMapping("/create")
    public String createFeedPage(Model model) {
        model.addAttribute("feed", new FeedRequestDto(
                null,  // city
                null,  // startedAt
                null,  // endedAt
                null,  // title
                null,  // content
                null,  // cost
                null,  // tag
                List.of() // days
        ));
        return "feed/create";
    }

    /**
     * 새로운 피드를 생성합니다.
     *
     * @param feedRequestDto 피드 생성 요청 데이터
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 생성된 피드의 상세 페이지 뷰 이름
     */
    @PostMapping
    public String createFeed(@ModelAttribute @Validated FeedRequestDto feedRequestDto, Model model) {
        FeedResponseDto feedResponseDto = feedService.createFeed(feedRequestDto);
        model.addAttribute("feed", feedResponseDto);
        return "feed/detail";
    }

    /**
     * 특정 피드의 상세 정보를 조회합니다.
     *
     * @param feedId 조회할 피드의 ID
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 피드 상세 페이지의 뷰 이름
     */
    @GetMapping("/{feedId}")
    public String findFeed(@PathVariable Long feedId, Model model) {
        FeedResponseDto feedResponseDto = feedService.findFeed(feedId);
        model.addAttribute("feed", feedResponseDto);
        return "feed/detail";
    }

    /**
     * 피드 수정 페이지를 렌더링합니다.
     *
     * @param feedId 수정할 피드의 ID
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 피드 수정 페이지의 뷰 이름
     */
    @GetMapping("/{feedId}/update")
    public String updateFeedPage(@PathVariable Long feedId, Model model) {
        FeedResponseDto feedResponseDto = feedService.findFeed(feedId);
        model.addAttribute("feed", feedResponseDto);
        return "feed/update";
    }

    /**
     * 피드를 수정합니다.
     *
     * @param feedId 수정할 피드의 ID
     * @param feedRequestDto 피드 수정 요청 데이터
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 수정된 피드의 상세 페이지 뷰 이름
     */
    @PutMapping("/{feedId}")
    public String updateFeed(@PathVariable Long feedId, @ModelAttribute @Validated FeedRequestDto feedRequestDto, Model model) {
        FeedResponseDto feedResponseDto = feedService.updateFeed(feedId, feedRequestDto);
        model.addAttribute("feed", feedResponseDto);
        return "feed/detail";
    }

    /**
     * 특정 피드를 삭제합니다.
     *
     * @param feedId 삭제할 피드의 ID
     * @return 피드 목록 페이지로 리다이렉트
     */
    @DeleteMapping("/{feedId}")
    public String deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
        return "redirect:/feeds";
    }

    @GetMapping("/countries")
    public String showCountriesPage() {
        return "feed/countries"; // templates/countries.html을 반환
    }

    /**
     * 특정 지역의 피드 목록을 조회합니다.
     *
     * @param encodedCountry 조회할 지역의 이름 (옵션)
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 지역별 피드 목록 페이지의 뷰 이름
     */
    @GetMapping("/countries/{country}")
    public String findFeedsByRegion(@PathVariable("country") String encodedCountry, Model model) {
        String country = URLDecoder.decode(encodedCountry, StandardCharsets.UTF_8);
        System.out.println("디코딩된 국가명: " + country);
        List<RegionResponseDto> regionFeeds = feedService.findRegion(country);
        System.out.println("조회된 피드 리스트: " + regionFeeds);
        model.addAttribute("feeds", regionFeeds);
        model.addAttribute("region", country);
        return "feed/country-feeds";
    }

    /**
     * 일정 추가 페이지를 렌더링합니다.
     *
     * @param feedId 피드의 ID
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 일정 추가 페이지의 뷰 이름
     */
    @GetMapping("/{feedId}/days/create")
    public String createDayPage(@PathVariable Long feedId, Model model) {
        model.addAttribute("feedId", feedId);
        return "feed/day-create";
    }

    /**
     * 일정 추가를 처리합니다.
     *
     * @param feedId 피드의 ID
     * @param daysRequestDto 일정 추가 요청 데이터
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 수정된 피드의 상세 페이지 뷰 이름
     */
    @PostMapping("/{feedId}/days")
    public String createDay(@PathVariable Long feedId, @ModelAttribute @Validated DaysRequestDto daysRequestDto, Model model) {
        FeedResponseDto feedResponseDto = feedService.createDay(feedId, daysRequestDto);
        model.addAttribute("feed", feedResponseDto);
        return "feed/detail";
    }

    public String getFeedDetail(@PathVariable Long id, Model model) {
        FeedResponseDto feed = feedService.findFeed(id);
        model.addAttribute("feed", feed);
        return "detail";
    }

    /**
     * 활동 추가 페이지를 렌더링합니다.
     *
     * @param feedId 피드의 ID
     * @param daysId 일정의 ID
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 활동 추가 페이지의 뷰 이름
     */
    @GetMapping("/{feedId}/days/{daysId}/activities/create")
    public String createActivityPage(@PathVariable Long feedId, @PathVariable Long daysId, Model model) {
        model.addAttribute("feedId", feedId);
        model.addAttribute("daysId", daysId);
        return "feed/activity-create";
    }

    /**
     * 활동 추가를 처리합니다.
     *
     * @param feedId 피드의 ID
     * @param daysId 일정의 ID
     * @param activityRequestDto 활동 추가 요청 데이터
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 수정된 피드의 상세 페이지 뷰 이름
     */
    @PostMapping("/{feedId}/days/{daysId}/activities")
    public String createActivity(@PathVariable Long feedId, @PathVariable Long daysId, @ModelAttribute @Validated ActivityRequestDto activityRequestDto, Model model) {
        FeedResponseDto feedResponseDto = feedService.createActivity(feedId, daysId, activityRequestDto);
        model.addAttribute("feed", feedResponseDto);
        return "feed/detail";
    }

    /**
     * 활동 수정 페이지를 렌더링합니다.
     *
     * @param feedId 피드의 ID
     * @param daysId 일정의 ID
     * @param activityId 활동의 ID
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 활동 수정 페이지의 뷰 이름
     */
    @GetMapping("/{feedId}/days/{daysId}/activities/{activityId}/update")
    public String updateActivityPage(@PathVariable Long feedId, @PathVariable Long daysId, @PathVariable Long activityId, Model model) {
        model.addAttribute("feedId", feedId);
        model.addAttribute("daysId", daysId);
        model.addAttribute("activityId", activityId);
        return "feed/activity-update";
    }

    /**
     * 활동 수정을 처리합니다.
     *
     * @param feedId 피드의 ID
     * @param daysId 일정의 ID
     * @param activityId 활동의 ID
     * @param activityRequestDto 활동 수정 요청 데이터
     * @param model 뷰에 데이터를 전달하기 위한 모델 객체
     * @return 수정된 피드의 상세 페이지 뷰 이름
     */
    @PutMapping("/{feedId}/days/{daysId}/activities/{activityId}")
    public String updateActivity(@PathVariable Long feedId, @PathVariable Long daysId, @PathVariable Long activityId, @ModelAttribute @Validated ActivityRequestDto activityRequestDto, Model model) {
        FeedResponseDto feedResponseDto = feedService.updateActivity(feedId, daysId, activityId, activityRequestDto);
        model.addAttribute("feed", feedResponseDto);
        return "feed/detail";
    }
}