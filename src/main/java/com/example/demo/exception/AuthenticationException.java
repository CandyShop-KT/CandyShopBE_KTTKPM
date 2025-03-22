package com.example.demo.exception;

public class AuthenticationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public AuthenticationException(String message) {
		super(message);
	}
	
}
