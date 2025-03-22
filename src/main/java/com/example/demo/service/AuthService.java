package com.example.demo.service;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.dto.SendOtpRequest;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.AuthenticationException;
import com.example.demo.model.User;

public interface AuthService {
	
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception, AuthenticationException;

	public User register(RegisterRequestDTO registerRequestDTO) throws Exception, ResourceConflictException;
	
	public void sendOTP(SendOtpRequest email) throws Exception;
	
}
