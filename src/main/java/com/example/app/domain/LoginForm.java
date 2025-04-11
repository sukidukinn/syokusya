package com.example.app.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {
    @NotBlank(message = "メールアドレスを入力してください")
    private String email;

    @NotBlank(message = "パスワードを入力してください")
    private String password;
}