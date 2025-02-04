package com.shineidle.tripf.mail.service;

// TODO : 즉시 주석 !
public interface MailService {

    void sendTopFeedsMail();

    void sendTopFeedsMailNow();

    void sendSimpleMail(String to, String subject, String text);
}
