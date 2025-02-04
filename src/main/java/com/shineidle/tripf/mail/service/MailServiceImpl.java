package com.shineidle.tripf.mail.service;

import com.shineidle.tripf.like.dto.FeedLikeDto;
import com.shineidle.tripf.like.service.LikeService;
import com.shineidle.tripf.user.service.UserService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender mailSender;
    private final LikeService likeService;
    private final UserService userService;
    private final TemplateEngine templateEngine;
    private final RedissonClient redissonClient;

    private static final String MAIL_LOCK_KEY = "sendTopFeedsMailLock";

    @Override
    @Scheduled(cron = "0 0 14 * * MON", zone = "Asia/Seoul")
    public void sendTopFeedsMail() {

        RLock lock = redissonClient.getLock(MAIL_LOCK_KEY);
        boolean acquired = false;

        try {
            // 10초 동안 락을 기다리고, 락을 획득하면 1분 후 자동 해제
            acquired = lock.tryLock(10, 60, TimeUnit.SECONDS);

            if (!acquired) {
                // 다른 서버에서 실행 중이면 중단
                return;
            }

            // 오늘이 월요일인지 추가 검증 (예방 차원)
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            if (now.getDayOfWeek() != DayOfWeek.MONDAY || now.getHour() != 14) {
                return; // 월요일 오후 2시가 아니면 실행하지 않음
            }

            // 상위 5개의 피드와 이메일 수신자 목록 가져오기
            List<FeedLikeDto> topFeeds = likeService.getTop5LikedFeedsWithImages();
            List<String> recipientEmails = userService.getActiveUserEmails();


            String adminEmail = "mail.tripf@gmail.com";
            if (!recipientEmails.contains(adminEmail)) {
                recipientEmails.add(adminEmail);
            }

            if (recipientEmails.isEmpty()) {
                return; // 수신자가 없으면 중단
            }

            // Thymeleaf Context 생성
            Context context = new Context();
            context.setVariable("feeds", topFeeds);
            context.setVariable("defaultImage", "cid:defaultImage");

            // HTML 템플릿 렌더링
            String htmlContent = templateEngine.process("top-feeds-email", context);

            // 테스트용 (실제 운영에서는 recipientEmails를 사용)
            for (String email : recipientEmails) {
                sendHtmlMail(email, "Tripf 오늘의 상위 5개 피드", htmlContent);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (acquired) {
                lock.unlock(); // 락 해제
            }
        }
    }

    private void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML 설정

            // 이미지 추가 (인라인으로 첨부)
            helper.addInline("backgroundImage", new ClassPathResource("static/images/mail/image-1.png"));
            helper.addInline("defaultImage", new ClassPathResource("static/images/mail/image-3.png"));
            helper.addInline("facebookImage", new ClassPathResource("static/images/mail/image-6.png"));
            helper.addInline("twitterImage", new ClassPathResource("static/images/mail/image-7.png"));
            helper.addInline("LinkedInImage", new ClassPathResource("static/images/mail/image-8.png"));
            helper.addInline("InstaImage", new ClassPathResource("static/images/mail/image-9.png"));

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