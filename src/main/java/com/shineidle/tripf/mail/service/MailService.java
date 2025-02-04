package com.shineidle.tripf.mail.service;

public interface MailService {
    /**
     * 상위 피드 정기 메일 발송
     */
    void sendTopFeedsMail();

    /**
     * 상위 피드 즉시 메일 발송
     */
    void sendTopFeedsMailNow();

    /**
     * 테스트 메일 발송
     */
    void sendSimpleMail(String to, String subject, String text);
}
