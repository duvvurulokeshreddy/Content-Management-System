package com.example.cms.exception;

public class BlogAlreadyExistsByTitleException extends RuntimeException{

	private String message;
	public BlogAlreadyExistsByTitleException(String message) {
		this.message = message;
	}



	public String getMessage() {
		return message;
	}
	
	
}
