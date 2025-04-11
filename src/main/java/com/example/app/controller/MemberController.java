package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.Member;
import com.example.app.validation.RegisterGroup;

@Controller
public class MemberController {
	//@Autowired
	//private UserMapper userMapper;
	
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
}