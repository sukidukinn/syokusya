package com.example.app.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MealPostIngredient {
	private Integer id;
	private Integer mealPostId;
	private Integer nutritionFoodId;
	private BigDecimal amountGrams;
}