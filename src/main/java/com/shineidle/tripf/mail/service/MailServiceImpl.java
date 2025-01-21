package com.shineidle.tripf.mail.service;

import com.shineidle.tripf.like.dto.FeedLikeDto;
import com.shineidle.tripf.like.service.LikeService;
import com.shineidle.tripf.user.service.UserService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender mailSender;
    private final LikeService likeService;
    private final UserService userService;
    private final TemplateEngine templateEngine;

    @Override
    public void sendTopFeedsMail() {
        // 상위 5개의 피드와 이메일 수신자 목록 가져오기
        List<FeedLikeDto> topFeeds = likeService.getTop5LikedFeedsWithImages();
        List<String> recipientEmails = userService.getActiveUserEmails();

        // Thymeleaf Context 생성
        Context context = new Context();
        context.setVariable("feeds", topFeeds);
        context.setVariable("defaultImage", "cid:defaultImage");

        // HTML 템플릿 렌더링
        String htmlContent = templateEngine.process("top-feeds-email", context);

//        // 이메일 발송
//        for (String email : recipientEmails) {
//            sendHtmlMail(email, "Tripf 오늘의 상위 5개 피드", htmlContent);
//        }

        // 테스트용
        sendHtmlMail(
                "chews26@naver.com",
                "Tripf 오늘의 상위 5개 피드 - 테스트",
                htmlContent
        );
    }

    private void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML 설정

            // 이미지 추가 (인라인으로 첨부)
            helper.addInline("backgroundImage", new ClassPathResource("static/images/image-1.png"));
            helper.addInline("defaultImage", new ClassPathResource("static/images/image-3.png"));
            helper.addInline("facebookImage", new ClassPathResource("static/images/image-6.png"));
            helper.addInline("twitterImage", new ClassPathResource("static/images/image-7.png"));
            helper.addInline("LinkedInImage", new ClassPathResource("static/images/image-8.png"));
            helper.addInline("InstaImage", new ClassPathResource("static/images/image-9.png"));

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
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