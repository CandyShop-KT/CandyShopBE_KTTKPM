package com.example.demo.service.imp;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.dto.SendOtpRequest;
import com.example.demo.exception.ResourceConflictException;
import com.example.demo.exception.AuthenticationException;
import com.example.demo.model.User;
import com.example.demo.model.enums.Gender;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.EmailService;
import com.example.demo.service.RedisService;
import com.example.demo.util.JwtUtil;

@Service
public class AuthServiceImp implements AuthService {

	private static final String DIGITS = "0123456789";
	private static final int OTP_LENGTH = 6;

	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private EmailService emailService;
	private RedisService redisService;
	@Value("${spring.mail.username}")
	private String myEmail;
	private JwtUtil jwtUtil;

	public AuthServiceImp(AuthenticationManager authenticationManager, UserRepository userRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService, RedisService redisService,
			JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.emailService = emailService;
		this.redisService = redisService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	@Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserName(), loginRequestDTO.getPassword()));
			User user = userRepository.findByUserName(loginRequestDTO.getUserName())
					.orElseThrow(() -> new AuthenticationException("User not found"));
			String token = jwtUtil.generateToken(user);
			return new LoginResponseDTO(token, user);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("Invalid username or password");
		}
	}

	private LoginResponseDTO buildLoginResponse(User user, String token) {
	    LoginResponseDTO response = new LoginResponseDTO();
	    response.setUserId(user.getUserId());
	    response.setUserName(user.getUserName());
	    response.setFirstName(user.getFirstName());
	    response.setLastName(user.getLastName());
	    response.setPhoneNumber(user.getPhoneNumber());
	    response.setEmail(user.getEmail());
	    response.setGender(user.getGender());
	    response.setAvatarUrl(user.getAvatarUrl());
	    response.setBirthDay(user.getBirthDay());
	    response.setCreatedAt(user.getCreatedAt());
	    response.setUpdatedAt(user.getUpdatedAt());
	    response.setRole(user.getRole());
	    response.setToken(token);
	    return response;
	}


	@Transactional
	@Override
	public User register(RegisterRequestDTO registerRequestDTO) throws Exception, ResourceConflictException {
		try {

			if (userRepository.existsByUserName(registerRequestDTO.getUserName()) == true)
				throw new ResourceConflictException("userName", "Username already exists");

			if (userRepository.existsByEmail(registerRequestDTO.getEmail()) == true)
				throw new ResourceConflictException("email", "Email already exists");

			if (userRepository.existsByPhoneNumber(registerRequestDTO.getPhoneNumber()) == true)
				throw new ResourceConflictException("phoneNumber", "Phone number already exists");

			User user = new User();
			user.setUserName(registerRequestDTO.getUserName());
			user.setFirstName(registerRequestDTO.getFirstName());
			user.setLastName(registerRequestDTO.getLastName());
			user.setPhoneNumber(registerRequestDTO.getPhoneNumber());
			user.setEmail(registerRequestDTO.getEmail());
			user.setGender(Gender.valueOf(registerRequestDTO.getGender()));
			user.setPassword(bCryptPasswordEncoder.encode(registerRequestDTO.getPassword()));
			user.setBirthDay(registerRequestDTO.getBirthDay());

			return userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void sendOTP(SendOtpRequest email) throws Exception {
		String otp = generateOTP();
		redisService.setWithExpireTime(String.format("otp?email=%s", email.getEmail()), otp, 60, TimeUnit.SECONDS);
		emailService.sendEmailVerifyOTP(myEmail, email.getEmail(), otp);
	}

	private String generateOTP() {
		SecureRandom random = new SecureRandom();
		StringBuilder otp = new StringBuilder(OTP_LENGTH);

		for (int i = 0; i < OTP_LENGTH; i++) {
			int index = random.nextInt(DIGITS.length());
			otp.append(DIGITS.charAt(index));
		}
		return otp.toString();
	}

}
