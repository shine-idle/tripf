package com.shineidle.tripf.domain.mail.service;

public interface MailService {
    /**
     * 상위 피드 정기 메일 발송
     */
    void sendTopFeedsMail();

    /**
     * 상위 피드 즉시 메일 발송
     */
    void sendTopFeedsMailNow();

}
