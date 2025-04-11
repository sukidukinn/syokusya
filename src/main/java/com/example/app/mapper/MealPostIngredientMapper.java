package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.MealPostIngredient;

@Mapper
public interface MealPostIngredientMapper {
	List<MealPostIngredient> selectByMealPostId(int mealPostId); // @Select削除！

	void deleteByMealPostId(int mealPostId);

	void insert(MealPostIngredient ingredient);
}