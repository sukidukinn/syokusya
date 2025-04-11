package com.example.app.domain;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class NutritionFood {
	private Integer id;
	private Integer foodCode;
	private String foodName;
	private String groupCode;
	private String groupName;
	private BigDecimal energyKcal;
	private BigDecimal proteinG;
	private BigDecimal fatG;
	private BigDecimal carbohydrateG;
	private BigDecimal fiberG;
	private BigDecimal saltG;
	private BigDecimal cholesterolMg;
	private String remarks;
}