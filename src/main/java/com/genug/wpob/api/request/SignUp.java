package com.genug.wpob.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUp {

    private String email;
    private String password;

    @Builder
    public SignUp(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
