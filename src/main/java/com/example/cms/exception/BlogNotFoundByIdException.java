package com.example.cms.exception;

public class BlogNotFoundByIdException extends RuntimeException {

	private String message;

	public BlogNotFoundByIdException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	
	
}
