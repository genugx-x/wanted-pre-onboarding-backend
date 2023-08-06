package com.genug.wpob.api.exception;

public class AuthorizationException extends ApiException {

    private static final String MESSAGE = "요청하신 작업을 수행할 권한이 없습니다.";

    public AuthorizationException() {
        super(MESSAGE);
    }


    public AuthorizationException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 403; // Forbidden
    }
}
