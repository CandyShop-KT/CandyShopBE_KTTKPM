package com.example.demo.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private String field;

	public BadRequestException(String field, String message) {
		super(message);
		this.field = field;
	}

}
