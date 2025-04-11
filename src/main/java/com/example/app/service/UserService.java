package com.example.app.service;

import com.example.app.domain.User;

public interface UserService {

	/**
	 * メールアドレスでユーザーを検索
	 */
	User findByEmail(String email);

	/**
	 * ユーザーIDでユーザーを検索
	 */
	User findById(int id);
	
	/**
	 * ログイン時に呼び出して最終ログイン日時を更新
	 */
	void updateLastLogin(int userId);

	/**
	 * ログアウト時に呼び出して最終ログアウト日時を更新
	 */
	void updateLastLogout(int userId);

}