package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.MealPostType;

public interface MealPostTypeMapper {
	List<MealPostType> selectAll() throws Exception;
}