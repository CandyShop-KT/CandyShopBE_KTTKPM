package com.example.demo.exception;

public class CustomException extends RuntimeException {
	public CustomException(String message) {
		super(message);
	}
	public CustomException(String messString, Throwable cause) {
		super(messString,cause);
	}

}
