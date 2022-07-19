package com.example.demo.epub;

public class EpubException extends RuntimeException {
	public EpubException(String message, Throwable cause) {
		super(message, cause);
	}

	public EpubException(Throwable cause) {
		super(cause);
	}
}
