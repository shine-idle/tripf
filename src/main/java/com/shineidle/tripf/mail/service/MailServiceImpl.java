package com.shineidle.tripf.mail.service;

import com.shineidle.tripf.like.dto.FeedLikeDto;
import com.shineidle.tripf.like.service.LikeService;
import com.shineidle.tripf.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender mailSender;
    private final LikeService likeService;
    private final UserService userService;

    @Override
//    @Scheduled(cron = "0 0 14 ? * MON-FRI") // 평일 오후 2시
    public void sendTopFeedsMail() {
        List<FeedLikeDto> topFeeds = likeService.getTop5LikedFeeds();
        List<String> recipientEmails = userService.getActiveUserEmails(); // UserService 통해 이메일 목록 가져오기

        StringBuilder feedContent = new StringBuilder("상위 5개 피드:\n");
        for (FeedLikeDto feed : topFeeds) {
            feedContent .append("제목: ").append(feed.getTitle())
                    .append("\n좋아요 수: ").append(feed.getLikeCount())
                    .append("\n\n");
        }

        // Todo 실제 유저 이메일 발송용
//        for (String email : recipientEmails) {
//            sendSimpleMail(
//                    email,
//                    "오늘의 상위 5개 피드",
//                    feedContent .toString()
//            );
//        }

        // Todo test용
        sendSimpleMail(
                "chews26@naver.com", // 수신자 이메일
                "오늘의 상위 5개 피드",    // 제목
                feedContent .toString()  // 본문
        );
    }

    /**
     * 테스트 메일 발송
     *
     * @param to 수신자 메일 주소
     * @param subject 메일 제목
     * @param text 메일 내용
     */
    @Override
    public void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}