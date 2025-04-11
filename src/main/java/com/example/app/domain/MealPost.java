package com.example.app.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MealPost {
	private Integer id;
	private Integer userId;

	@NotBlank(message = "食事名を入力してください")
	private String mealName;

	@NotNull(message = "食事時間を入力してください")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime mealTime;

	private String photoPath;
	private String memo;

	@DecimalMin(value = "0.0", message = "カロリーは0以上で入力してください")
	@DecimalMax(value = "50000.0", message = "カロリーは50000kcal以下で入力してください")
	private BigDecimal totalCalorie;

	@DecimalMin(value = "0.0", message = "重量は0以上で入力してください")
	@DecimalMax(value = "30000.0", message = "重量は30000g以下で入力してください")
	private BigDecimal totalGrams;

	@DecimalMin(value = "0.0", message = "たんぱく質は0以上で入力してください")
	@DecimalMax(value = "5000.0", message = "たんぱく質は5000g以下で入力してください")
	private BigDecimal proteinG;

	@DecimalMin(value = "0.0", message = "脂質は0以上で入力してください")
	@DecimalMax(value = "3000.0", message = "脂質は3000g以下で入力してください")
	private BigDecimal fatG;

	@DecimalMin(value = "0.0", message = "炭水化物は0以上で入力してください")
	@DecimalMax(value = "10000.0", message = "炭水化物は10000g以下で入力してください")
	private BigDecimal carbohydrateG;

	@DecimalMin(value = "0.0", message = "食物繊維は0以上で入力してください")
	@DecimalMax(value = "1000.0", message = "食物繊維は1000g以下で入力してください")
	private BigDecimal fiberG;

	@DecimalMin(value = "0.0", message = "塩分は0以上で入力してください")
	@DecimalMax(value = "500.0", message = "塩分は500g以下で入力してください")
	private BigDecimal saltG;

	@DecimalMin(value = "0.0", message = "コレステロールは0以上で入力してください")
	@DecimalMax(value = "5000.0", message = "コレステロールは5000mg以下で入力してください")
	private BigDecimal cholesterolMg;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime created;

	private Boolean deleted;
}