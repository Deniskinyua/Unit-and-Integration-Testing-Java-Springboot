package com.testing.junitmockitotesting.UnitTests.Fake.Services;

public class EmailService {

    private final EmailService emailService;

    public EmailService(EmailService emailService){
        this.emailService = emailService;
    }

    public void sendMessage(String message){
        throw new AssertionError("Method not implemented");
    }

}
