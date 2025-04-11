package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.app.domain.User;

@Mapper
public interface UserMapper {
	List<User> selectUsers();

	User selectById(int id);

	int addUser(User user);

	int updateUser(User user);

	int deleteUser(int id);

	User findByEmail(@Param("email") String email);

	void updateLastLogin(@Param("id") int id);

	void updateLastLogout(@Param("id") int id);
}