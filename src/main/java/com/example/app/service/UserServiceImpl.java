package com.example.app.service;

import org.springframework.stereotype.Service;

import com.example.app.domain.User;
import com.example.app.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;

	public UserServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public User findByEmail(String email) {
		return userMapper.findByEmail(email);
	}

	@Override
	public void updateLastLogin(int id) {
		userMapper.updateLastLogin(id);
	}

	@Override
	public void updateLastLogout(int id) {
		userMapper.updateLastLogout(id);
	}

	@Override
	public User findById(int id) {
		return userMapper.selectById(id);
	}
}