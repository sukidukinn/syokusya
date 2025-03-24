package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.Member;
import com.example.app.mapper.MemberMapper;
import com.example.app.validation.LoginGroup;
import com.example.app.validation.RegisterGroup;

import jakarta.validation.Valid;

@Controller
public class MemberController {
	private final MemberMapper mapper = null;

	// 会員一覧の表示
	@GetMapping("/")
	public String list(Model model) throws Exception {
		model.addAttribute("members", mapper.selectMembers());
		return "members";
	}

	// 会員追加フォームの表示
	@GetMapping("/add")
	public String add(Model model) throws Exception {
		Member member = new Member();
		member.setAge(20);
		member.setTypeId(1);
		model.addAttribute("member", member);
		return "addMember";
	}

	// 会員追加
	@PostMapping("/add")
	public String add(@Valid Member member, Errors errors) throws Exception {
		if (errors.hasErrors()) {
			return "addMember";
		}

		mapper.addMember(member);
		return "redirect:/";
	}

	@GetMapping("/register")
	public String registerGet(Model model) {
		model.addAttribute("member", new Member());
		return "register";
	}

	@PostMapping("/register")
	public String registerPost(
			@Validated(RegisterGroup.class) Member member,
			Errors errors) {
		if (!member.getEmail().equals(member.getEmailConf())) {
			errors.rejectValue("emailConf", "error.email.inequal");
		}
		if (errors.hasErrors()) {
			return "register";
		}
		return "registerDone";
	}

	@GetMapping("/login")
	public String loginGet(Model model) {
		model.addAttribute("loginMember", new Member());
		return "login";
	}

	@PostMapping("/login")
	public String loginPost(
			@Validated(LoginGroup.class) @ModelAttribute("loginMember") Member loginMember,
			Errors errors) {
		if (errors.hasErrors()) {
			return "login";
		}
		// 未入力でない場合、ID とパスワードをチェック
		if (!loginMember.getEmail().equals("taro")
				|| !loginMember.getPassword().equals("abcd")) {
			// グローバルエラーの追加
			errors.reject("error.wrong_id_or_password");
			return "login";
		}
		return "loginDone";
	}
}
