package com.boot.store.exception;

/**
 * token异常
 *
 */
public class TokenException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2483599972506697943L;

	public TokenException() {
		super();
	}

	public TokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenException(String message) {
		super(message);
	}

	public TokenException(Throwable cause) {
		super(cause);
	}
}