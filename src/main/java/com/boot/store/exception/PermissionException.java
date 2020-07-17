package com.boot.store.exception;

/**
 * 权限异常
 *
 */
public class PermissionException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2483599972506697941L;

	public PermissionException() {
		super();
	}

	public PermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionException(String message) {
		super(message);
	}

	public PermissionException(Throwable cause) {
		super(cause);
	}
}