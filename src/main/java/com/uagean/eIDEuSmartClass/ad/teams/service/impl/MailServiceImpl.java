/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uagean.eIDEuSmartClass.ad.teams.service.impl;

import com.uagean.eIDEuSmartClass.ad.teams.service.MailService;
import com.uagean.eIDEuSmartClass.ad.teams.util.MailContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author nikos
 */
@Service
public class MailServiceImpl implements MailService {

    private final static String MAIL_FRIENDLY_NAME = " i4mLab@UAegean";
    private final String FROM = "smartclass@i4mlabUAegean.onmicrosoft.com";
    private static Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String prepareAndSendEmail(String recipient) {
        log.info("Sending email to " + recipient);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(recipient);
            helper.setFrom(new InternetAddress(FROM, MAIL_FRIENDLY_NAME));
            helper.setSubject("WCourse on e-Privacy and Cybersecurity: You can now connect to Teams (Microsoft)");
            String content = MailContentBuilder.buildEmail();

            helper.setText(content, true);

            mailSender.send(message);

            return "OK";
        } catch (Exception e) {
            log.info("Error sending mail", e);
            return "ERROR";
        }
    }

}
