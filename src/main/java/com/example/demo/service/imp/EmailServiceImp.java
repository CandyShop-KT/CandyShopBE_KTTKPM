package com.example.demo.service.imp;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImp implements EmailService {

	private final JavaMailSender javaMailSender;

	public EmailServiceImp(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	@Override
	public void sendEmailVerifyOTP(String from, String to,  String verificationCode)
			throws MessagingException {
		MimeMessage mail = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject("CandyShop - Email Verification");
		String content = "<h1>CandyShop - Email Verification</h1>" + "<p>Your email verification code is: <b>"
				+ verificationCode + "</b></p>";
		helper.setText(content, true);
		javaMailSender.send(mail);
	}

}
