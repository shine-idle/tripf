package com.shineidle.tripf.mail.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.LockErrorCode;
import com.shineidle.tripf.common.exception.type.MailErrorCode;
import com.shineidle.tripf.like.dto.FeedLikeDto;
import com.shineidle.tripf.like.service.LikeService;
import com.shineidle.tripf.user.service.UserService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final LikeService likeService;
    private final UserService userService;
    private final TemplateEngine templateEngine;
    private final RedissonClient redissonClient;

    private static final String MAIL_LOCK_KEY = "sendTopFeedsMailLock";

    /**
     * 상위 피드 정기 메일 발송
     * 매주 14시 월요일 메일 발송
     * 관리자 이메일 : chews26@naver.com
     */
    @Override
    @Scheduled(cron = "0 0 14 * * MON", zone = "Asia/Seoul")
    public void sendTopFeedsMail() {
        log.info("📩 sendTopFeedsMail() 실행됨");

        RLock lock = redissonClient.getLock(MAIL_LOCK_KEY);
        boolean acquired = false;

        try {
            acquired = lock.tryLock(10, 60, TimeUnit.SECONDS);

            if (!acquired) {
                throw new GlobalException(LockErrorCode.LOCK_ACQUISITION_FAILED);
            }

            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            if (now.getDayOfWeek() != DayOfWeek.MONDAY || now.getHour() != 14) {
                throw new GlobalException(MailErrorCode.TIME_INVALID);
            }

            List<FeedLikeDto> topFeeds = likeService.getTop5LikedFeedsWithImages();
            List<String> recipientEmails = userService.getActiveUserEmails();

            if (topFeeds.isEmpty()) {
                throw new GlobalException(MailErrorCode.MAIL_EMPTY);
            }

            String adminEmail = "chews26@naver.com";
            if (!recipientEmails.contains(adminEmail)) {
                recipientEmails.add(adminEmail);
            }

            Context context = new Context();
            context.setVariable("feeds", topFeeds);
            context.setVariable("defaultImage", "cid:defaultImage");

            String htmlContent = templateEngine.process("mail/top-feeds-email", context);

            for (String email : recipientEmails) {
                sendHtmlMail(email, "Tripf 오늘의 상위 5개 피드", htmlContent);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GlobalException(LockErrorCode.LOCK_INTERRUPTED);
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }
    }

    /**
     * 상위 피드 즉시 메일 발송
     * 관리자 이메일 : chews26@naver.com
     */
    @Override
    public void sendTopFeedsMailNow() {
        RLock lock = redissonClient.getLock(MAIL_LOCK_KEY);
        boolean acquired = false;

        try {
            acquired = lock.tryLock(10, 60, TimeUnit.SECONDS);

            if (!acquired) {
                throw new GlobalException(LockErrorCode.LOCK_ACQUISITION_FAILED);
            }

            List<FeedLikeDto> topFeeds = likeService.getTop5LikedFeedsWithImages();
            List<String> recipientEmails = userService.getActiveUserEmails();


            String adminEmail = "chews26@naver.com";
            if (!recipientEmails.contains(adminEmail)) {
                recipientEmails.add(adminEmail);
            }

            Context context = new Context();
            context.setVariable("feeds", topFeeds);
            context.setVariable("defaultImage", "cid:defaultImage");

            String htmlContent = templateEngine.process("mail/top-feeds-email", context);

            for (String email : recipientEmails) {
                sendHtmlMail(email, "Tripf 오늘의 상위 5개 피드", htmlContent);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GlobalException(LockErrorCode.LOCK_INTERRUPTED);
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }
    }

    /**
     * HTML 형식의 이메일을 전송합니다.
     *
     * @param to          수신자 이메일 주소
     * @param subject     이메일 제목
     * @param htmlContent 이메일 본문 (HTML 형식)
     */
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
     * @param to      수신자 메일 주소
     * @param subject 메일 제목
     * @param text    메일 내용
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