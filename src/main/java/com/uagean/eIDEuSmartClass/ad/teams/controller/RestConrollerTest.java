package com.uagean.eIDEuSmartClass.ad.teams.controller;

import com.uagean.eIDEuSmartClass.ad.teams.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestConrollerTest {

    @Autowired
    private MailService mailService;

    @RequestMapping(value="/sendEmail")
    public void testSendEmail(){
        mailService.prepareAndSendEmail("heyjey2@hotmail.com");
    }
}
