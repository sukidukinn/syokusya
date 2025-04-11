package com.example.app.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private Integer id;

	@NotBlank(message = "名前を入力してください")
	@Size(max = 10, message = "10字以内で入力してください")
	private String name;

	@Min(value = 0, message = "年齢は0以上で入力してください")
	private Integer age;

	@Size(max = 255, message = "住所は255字以内で入力してください")
	private String address;

	@NotNull(message = "会員種別を選択してください")
	private Integer typeId;

	@NotNull(message = "性別を選択してください")
	@Range(min = 0, max = 2, message = "性別は0（男性）、1（女性）、2（その他）のいずれかで入力してください")
	private Integer sex;

	@Min(value = 0, message = "身長は0以上で入力してください")
	private Integer heightCm;

	@Min(value = 0, message = "体重は0以上で入力してください")
	private Integer weightKg;

	@Min(value = 0, message = "目標体重は0以上で入力してください")
	private Integer goalWeightKg;

	// Java側で計算して設定する
	private BigDecimal bmi;

	private BigDecimal bmr;

	@DecimalMin(value = "1.0", message = "活動レベルは1.0以上で入力してください")
	@DecimalMax(value = "2.5", message = "活動レベルは2.5以下で入力してください")
	private BigDecimal activityLevel;

	@Size(max = 255, message = "自己紹介は255字以内で入力してください")
	private String bio;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	private String nickname;

	private String photoPath;

	private LocalDateTime lastLogin;

	private LocalDateTime lastLogout;

	private LocalDateTime created;

	private BigDecimal idealWeightKg; // 理想体重（BMI22）
	private BigDecimal weightDiffKg; // 現体重 - 目標体重
	private BigDecimal tdee; // 総消費カロリー
	private Integer calorieGoal; // 目標達成のための摂取目安kcal
	private Integer targetDays; // 目標日数
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate startDate; // 開始日
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate goalDate; // 目標日
	
	private String progressPhotoPath; // 写真ファイルパス
	private LocalDateTime lastUpdated; // 最終更新日時

	private Boolean deleted;
}