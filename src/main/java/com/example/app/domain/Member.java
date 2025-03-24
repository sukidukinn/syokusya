package com.example.app.domain;

import java.time.LocalDateTime;

import com.example.app.validation.LoginGroup;
import com.example.app.validation.RegisterGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Member {
	private Integer id;

	@NotBlank(message = "名前を入力してください")
	@Size(max = 10, message = "10 字以内で入力してください")
	private String name;
	
	@NotBlank(groups = { RegisterGroup.class, LoginGroup.class })
	@Email(groups = { RegisterGroup.class })
	private String email;
	@NotBlank(groups = { RegisterGroup.class })
	private String emailConf;
	@NotBlank(groups = { RegisterGroup.class, LoginGroup.class })
	private String password;
	
	@Min(value = 0,message = "0 以上の整数を入力してください")
	private Integer age;
	
	private String address;
	private Integer typeId;
	private LocalDateTime created;
}