package br.com.fiap.techchallenge.hackathonvideo.application.exceptions;

public class DoesNotExistException extends RuntimeException {

	public DoesNotExistException(String message) {
		super(message);
	}

}
