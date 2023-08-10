package com.genug.wpob.api.exception;

public class PostNotFoundException extends ApiException {

    private static final String MESSAGE = "존재하지 않는 게시글입니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }


    public PostNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 404; // not found
    }
}
