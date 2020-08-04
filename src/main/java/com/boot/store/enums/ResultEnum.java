package com.boot.store.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    TokenError(-1, "Invalid token"),
	PermissionError(-1, "Invalid permissions"),
	MissingServletRequestParameter(400,"Missing servletRequest parameter"),
    RequestBodyEmpty(400,"RequestBody Not Allow Empty"),
    TypeMismatchException(401,"Request parameter Type not match"),
    RequestMethodNotAllowed(405,"Request method not Allowed"),
    InterfaceNotExist(404,"Interface does not exist"),
	DbReadOnlyException(-1,"演示环境只能进行读操作！"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
