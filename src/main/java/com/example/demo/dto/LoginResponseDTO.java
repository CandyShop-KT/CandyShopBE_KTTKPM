package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.demo.model.enums.Gender;
import com.example.demo.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
	private String userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	private Gender gender;
	private String avatarUrl;
	private LocalDate birthDay;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Role role;
	private String token;
}
