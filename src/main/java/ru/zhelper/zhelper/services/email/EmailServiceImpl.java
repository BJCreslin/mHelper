package ru.zhelper.zhelper.services.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static final String FROM = "info@zhelper.ru";
    private static final String SENDING = "Sending email";
    private static final String SUCCESS = "Почта успешно отправлена";
    private static final String ERROR_SENDING = "Возникла исключительная ситуация при отправке почты";

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String to, String subject, String content) {
        logger.info(SENDING, to, subject, content);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
        logger.info(SUCCESS);
    }

    public void sendHtmlMail(String to, String subject, String content) {
        logger.info(SENDING, to, subject, content);
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(FROM);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            emailSender.send(message);
            logger.info(SUCCESS);
        } catch (MessagingException e) {
            logger.error(ERROR_SENDING, e);
        }
    }
}