package com.example.app.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.LoginForm;
import com.example.app.domain.User;
import com.example.app.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class LoginController {

	private final UserService userService;

	public LoginController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("loginMember", new LoginForm());
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("loginMember") @Valid LoginForm form,
			BindingResult result,
			HttpSession session) {

		if (result.hasErrors()) {
			return "login";
		}

		// メールアドレスで仮ユーザー取得（パスワード検証用）
		User loginUser = userService.findByEmail(form.getEmail());

		if (loginUser == null || !BCrypt.checkpw(form.getPassword(), loginUser.getPassword())) {
			result.reject("login.failed", "メールアドレスまたはパスワードが正しくありません");
			return "login";
		}

		if (loginUser.getTypeId() != null && loginUser.getTypeId() == 4) {
			result.reject("login.retired", "このアカウントは退会済みです。復活希望は syokusya@gmail.com まで。");
			return "login";
		}

		// ログイン時刻を更新
		userService.updateLastLogin(loginUser.getId());

		// ⭐ 最新のユーザー情報を再取得（TDEEなど含む）
		User freshUser = userService.findById(loginUser.getId());

		// セッションに保存
		session.setAttribute("loginUser", freshUser);

		return "redirect:/mealposts";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) throws Exception {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser != null) {
			userService.updateLastLogout(loginUser.getId());
		}
		session.invalidate(); // セッションを無効化
		return "redirect:/login"; // ログイン画面にリダイレクト
	}
}