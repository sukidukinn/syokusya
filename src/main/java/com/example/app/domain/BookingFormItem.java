package com.example.app.domain;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookingFormItem {
	// 代表者氏名
	@NotBlank
	@Size(max = 10)
	private String name;
	// メールアドレス
	@NotBlank
	@Email
	private String email;
	// 利用人数
	@NotNull
	@Range(min = 1, max = 10)
	private Integer number;
	// 利用する部屋
	private RoomType roomType;
	// 利用予定日
	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	// 利用時間帯
	@NotEmpty
	private List<Integer> time;
	// 駐車場利用の有無
	private Integer parking;
	// 施設利用規約への同意
	@AssertTrue
	private Boolean agreement;
}