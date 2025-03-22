package com.example.demo.service;

import jakarta.mail.MessagingException;

public interface EmailService {

	public void sendEmailVerifyOTP(String from, String to, String verificationCode)
			throws MessagingException;

}
