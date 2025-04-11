package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.NutritionFood;

public interface NutritionFoodMapper {
	List<NutritionFood> selectAll();
}