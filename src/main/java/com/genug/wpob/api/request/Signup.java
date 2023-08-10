package com.genug.wpob.api.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class Signup {

    @Pattern(regexp = "^.*@.*$", message="이메일 형식이 올바르지 않습니다.")
    private String email;

    @Length(min = 8, message="비밀번호 형식이 올바르지 않습니다.")
    private String password;

    @Builder
    public Signup(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
