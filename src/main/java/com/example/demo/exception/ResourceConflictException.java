package com.example.demo.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceConflictException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private String field;

	public ResourceConflictException(String field, String message) {
		super(message);
		this.field = field;
	}
	
}
