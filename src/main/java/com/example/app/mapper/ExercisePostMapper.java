package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.ExercisePost;

@Mapper
public interface ExercisePostMapper {
	List<ExercisePost> findAll();

	List<ExercisePost> findByUserId(@Param("userId") Integer userId);

	ExercisePost selectById(@Param("id") Integer id);

	void insert(ExercisePost post);

	void update(ExercisePost post);

	void delete(@Param("id") Integer id);
}