package com.example.app.controller;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.User;
import com.example.app.mapper.MealPostMapper;
import com.example.app.mapper.UserMapper;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	@Value("${upload.path}")
	private String uploadPath;

	private final UserMapper userMapper;

	@Autowired
	private MealPostMapper mealPostMapper;

	@GetMapping("/mealPosts")
	public String mealPostList(Model model, HttpSession session) throws Exception {
		// ログインチェック等…
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		model.addAttribute("mealPosts", mealPostMapper.findByUserId(loginUser.getId()));
		return "mealposts/list"; // HTMLファイル名に合わせて
	}

	// 会員一覧の表示
	@GetMapping("/users")
	public String list(Model model) throws Exception {
		model.addAttribute("users", userMapper.selectUsers());
		return "users";
	}

	// 会員追加
	@PostMapping("/add")
	public String add(@Valid User user, Errors errors) throws Exception {
		if (errors.hasErrors()) {
			return "addUser";
		}

		userMapper.addUser(user);
		return "redirect:/";
	}

	@GetMapping("/profile")
	public String showProfile(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		// ユーザー情報を取得し直して表示用に設定
		User user = userMapper.selectById(loginUser.getId());
		model.addAttribute("user", user);

		// pageMessageがFlashAttributeで来ていたら表示用に渡す
		if (!model.containsAttribute("pageMessage")) {
			model.addAttribute("pageMessage", "");
		}
		
		//System.out.println("📥 プロフィール更新リクエスト: " + user);
		
		return "profile";
	}

	@GetMapping("/profile/clearImage")
	public String clearProfileImage(HttpSession session, RedirectAttributes redirectAttrs) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser != null && loginUser.getPhotoPath() != null) {
			// 実ファイル削除
			File file = new File(uploadPath + "/" + loginUser.getPhotoPath().replace("uploads/", ""));
			if (file.exists())
				file.delete();

			// DB更新
			loginUser.setPhotoPath(null);
			userMapper.updateUser(loginUser);
			session.setAttribute("loginUser", loginUser);
			redirectAttrs.addFlashAttribute("pageMessage", "プロフィール画像を削除しました");
		}
		return "redirect:/profile";
	}

	@PostMapping("/profile/update")
	public String updateProfile(
			@Valid @ModelAttribute("user") User user,
			BindingResult bindingResult,
			@RequestParam("photoFile") MultipartFile photoFile,
			@RequestParam("action") String action,
			HttpSession session,
			RedirectAttributes redirectAttrs,
			Model model) throws Exception {

		User loginUser = (User) session.getAttribute("loginUser");
		user.setId(loginUser.getId());

		if (bindingResult.hasErrors()) {
			model.addAttribute("pageMessage", "入力内容に誤りがあります。");
			return "profile";
		}

		// パスワード処理省略（同じ）
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			user.setPassword(loginUser.getPassword());
		} else {
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		}

		// 写真アップロード処理
		if (!photoFile.isEmpty()) {
			String filename = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
			File destFile = new File(uploadPath, filename);
			photoFile.transferTo(destFile);
			user.setPhotoPath("uploads/" + filename);
		} else {
			user.setPhotoPath(loginUser.getPhotoPath()); // 既存画像を維持
		}

		if (user.getStartDate() != null && user.getTargetDays() != null) {
			LocalDate goalDate = user.getStartDate().plusDays(user.getTargetDays());
			user.setGoalDate(goalDate);
		}
		// 更新
		userMapper.updateUser(user);
		User updatedUser = userMapper.selectById(user.getId());
		session.setAttribute("loginUser", updatedUser);

		if ("saveAndBack".equals(action)) {
			redirectAttrs.addFlashAttribute("pageMessage", "プロフィールを更新しました");
			return "redirect:/mealPosts";
		}
		model.addAttribute("pageMessage", "プロフィールを更新しました");
		return "profile";
	}

	@GetMapping("/profile/withdraw")
	public String withdraw(HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}

		// typeId = 4 で「退会済み」として扱う
		loginUser.setTypeId(4);
		userMapper.updateUser(loginUser); // typeId を更新するようにMapperも対応済みであること

		// セッションを破棄
		session.invalidate();

		return "redirect:/login";
	}
}