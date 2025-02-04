package com.shineidle.tripf.mail.service;

public interface MailService {

    void sendTopFeedsMail();

    void sendTopFeedsMailNow();

    void sendSimpleMail(String to, String subject, String text);
}
