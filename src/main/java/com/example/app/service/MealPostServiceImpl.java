package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.MealPost;
import com.example.app.domain.MealPostType;
import com.example.app.mapper.MealPostMapper;
import com.example.app.mapper.MealPostTypeMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MealPostServiceImpl implements MealPostService {
	private final MealPostMapper mealPostMapper;
	private final MealPostTypeMapper mealPostTypeMapperMapper;

	@Override
	public List<MealPost> getMealPostList() throws Exception {
		return mealPostMapper.selectAll();
	}

	@Override
	public MealPost getMealPostById(Integer id) throws Exception {
		return mealPostMapper.selectById(id);
	}

	@Override
	public void addMealPost(MealPost member) throws Exception {
		mealPostMapper.insert(member);
	}

	@Override
	public void editMealPost(MealPost member) throws Exception {
		mealPostMapper.update(member);
	}

	@Override
	public void deleteMealPost(Integer id) throws Exception {
		mealPostMapper.delete(id);
	}

	@Override
	public List<MealPostType> getTypeList() throws Exception {
		return mealPostTypeMapperMapper.selectAll();
	}

	@Override
	public List<MealPost> getMealPostListByUserId(Integer userId) throws Exception {
		return mealPostMapper.findByUserId(userId);
	}

	@Override
	public void softDeleteMealPost(Integer id) throws Exception {
		mealPostMapper.softDeleteById(id);
	}

	@Override
	public List<MealPost> getMealPostsByUserId(int userId) throws Exception {
		return mealPostMapper.findByUserId(userId);
	}
}
