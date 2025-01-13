package com.shineidle.tripf.mail.service;

public interface MailService {

    void sendSimpleMail(String to, String subject, String text);
}
