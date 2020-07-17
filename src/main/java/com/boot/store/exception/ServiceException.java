package com.boot.store.exception;

/**
 * 业务异常
 *
 */
public class ServiceException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2483599972506697940L;

	public ServiceException() {
		super();
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}
}