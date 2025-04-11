package com.example.app.controller;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.ExercisePost;
import com.example.app.domain.MealPost;
import com.example.app.domain.MealPostIngredient;
import com.example.app.domain.NutritionFood;
import com.example.app.domain.User;
import com.example.app.mapper.ExercisePostMapper;
import com.example.app.mapper.MealPostIngredientMapper;
import com.example.app.mapper.MealPostMapper;
import com.example.app.mapper.NutritionFoodMapper;
import com.example.app.service.MealPostService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MealPostController {
	@Value("${upload.path}")
	private String uploadPath;

	private final MealPostService mealPostService; // ← 名前をmealPostServiceに統一

	@Autowired
	private MealPostMapper mealPostMapper;

	@Autowired
	private MealPostIngredientMapper mealPostIngredientMapper;

	@Autowired
	private NutritionFoodMapper nutritionFoodMapper;


	@Autowired
	private ExercisePostMapper exercisePostMapper;
	
	//保存処理
	@PostMapping("/mealPosts/save")
	public String saveMealPost(@Valid @ModelAttribute("mealPost") MealPost mealPost,
	                           BindingResult bindingResult,
	                           @RequestParam("photoFile") MultipartFile photoFile,
	                           @RequestParam(value = "action", required = false) String action,
	                           HttpSession session,
	                           Model model) throws Exception {

	    User loginUser = (User) session.getAttribute("loginUser");
	    if (loginUser == null) {
	        return "redirect:/login";
	    }

	    // 💥 バリデーションエラーがある場合、フォームに戻す
	    if (bindingResult.hasErrors()) {
	        // 💡 画像パスが null なら、既存の値を再設定（エラーで飛ばされたとき消えないように）
	        if (mealPost.getId() != null && (mealPost.getPhotoPath() == null || mealPost.getPhotoPath().isEmpty())) {
	            MealPost existing = mealPostMapper.selectById(mealPost.getId());
	            if (existing != null) {
	                mealPost.setPhotoPath(existing.getPhotoPath());
	            }
	        }

	        model.addAttribute("pageMessage", "入力に誤りがあります");
	        model.addAttribute("nutritionFoods", nutritionFoodMapper.selectAll());
	        model.addAttribute("mealPostIngredients", mealPostIngredientMapper.selectByMealPostId(mealPost.getId()));
	        return "mealposts/detail";
	    }

	    // 📸 写真アップロード処理
	    if (!photoFile.isEmpty()) {
	        String filename = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
	        File destFile = new File(uploadPath, filename);
	        photoFile.transferTo(destFile);
	        mealPost.setPhotoPath("uploads/" + filename);
	    } else if (mealPost.getId() != null) {
	        MealPost existing = mealPostMapper.selectById(mealPost.getId());
	        if (existing != null && existing.getPhotoPath() != null) {
	            mealPost.setPhotoPath(existing.getPhotoPath());
	        }
	    }

	    mealPost.setUserId(loginUser.getId());

	    if (mealPost.getId() == null) {
	        mealPostService.addMealPost(mealPost);
	    } else {
	        mealPostService.editMealPost(mealPost);
	    }

	    // 💾 保存のみ
	    if ("saveOnly".equals(action)) {
	        MealPost updated = mealPostMapper.selectById(mealPost.getId());
	        model.addAttribute("mealPost", updated);
	        model.addAttribute("nutritionFoods", nutritionFoodMapper.selectAll());
	        model.addAttribute("mealPostIngredients", mealPostIngredientMapper.selectByMealPostId(mealPost.getId()));
	        model.addAttribute("pageMessage", "食事情報を保存しました");
	        return "mealposts/detail";
	    }

	    // ✅ 戻る
	    return "redirect:/mealposts";
	}

	//食事投稿追加処理
	@GetMapping("/mealPosts/add")
	public String addMealPostForm(Model model) {
		MealPost emptyPost = new MealPost();
		emptyPost.setMealTime(LocalDateTime.now());
		model.addAttribute("mealPost", emptyPost);
		model.addAttribute("ingredients", List.of()); // 新規なので空

		List<NutritionFood> nutritionFoods = nutritionFoodMapper.selectAll();
		model.addAttribute("nutritionFoods", nutritionFoods);

		return "mealposts/detail";
	}

	//一覧表示処理
	@GetMapping("/mealposts")
	public String list(Model model, HttpSession session) throws Exception {
		User loginUser = (User) session.getAttribute("loginUser");
		if (loginUser == null) {
			return "redirect:/login";
		}
		
		List<ExercisePost> exercisePosts = exercisePostMapper.findByUserId(loginUser.getId());
		model.addAttribute("exercisePosts", exercisePosts);
		
		List<MealPost> posts;
		if (loginUser.getTypeId() == 3) { //運営モード
			posts = mealPostService.getMealPostList();
		} else {
			posts = mealPostService.getMealPostsByUserId(loginUser.getId());
		}

		model.addAttribute("mealPosts", posts);
		model.addAttribute("loginUser", loginUser);
		return "mealposts/list";
	}

	// 食事投稿削除
	@GetMapping("/mealPosts/delete/{id}")
	public String deleteMealPost(@PathVariable Integer id) throws Exception {
		mealPostMapper.softDeleteById(id);
		return "redirect:/mealposts";
	}

	// 画像のみ削除
	@GetMapping("/mealPosts/{id}/clearImage")
	public String clearMealPostImage(@PathVariable Integer id,
			RedirectAttributes redirectAttributes) throws Exception {
		MealPost post = mealPostMapper.selectById(id);
		if (post != null && post.getPhotoPath() != null) {
			// 実ファイルも削除
			File file = new File(uploadPath + "/" + post.getPhotoPath().replace("uploads/", ""));
			if (file.exists())
				file.delete();

			// DB上のphotoPathをnullに更新
			post.setPhotoPath(null);
			mealPostMapper.update(post);
			
			redirectAttributes.addFlashAttribute("pageMessage", "画像を削除しました");
		}
		return "redirect:/mealPosts/edit/" + id;
	}

	@GetMapping("/mealPosts/edit/{id}")
	public String editMealPost(@PathVariable Integer id, Model model) throws Exception {
		MealPost mealPost = mealPostMapper.selectById(id);
		if (mealPost == null) {
			return "redirect:/mealPosts";
		}

		List<MealPostIngredient> ingredients = mealPostIngredientMapper.selectByMealPostId(id);
		List<NutritionFood> nutritionFoods = nutritionFoodMapper.selectAll();

		model.addAttribute("mealPost", mealPost);
		model.addAttribute("ingredients", ingredients);
		model.addAttribute("nutritionFoods", nutritionFoods);

		return "mealposts/detail";
	}

	@GetMapping("/mealPosts/{mealPostId}/ingredients")
	@ResponseBody
	public List<MealPostIngredient> getIngredients(@PathVariable Integer mealPostId) {
		return mealPostIngredientMapper.selectByMealPostId(mealPostId);
	}

	@PostMapping("/mealPosts/updateIngredients")
	@ResponseBody
	public ResponseEntity<String> updateIngredients(@RequestBody List<MealPostIngredient> ingredients) {
		if (ingredients == null || ingredients.isEmpty()) {
			return ResponseEntity.badRequest().body("空のデータです");
		}

		Integer mealPostId = ingredients.get(0).getMealPostId();

		// 一度削除してから再登録（シンプルな方式）
		mealPostIngredientMapper.deleteByMealPostId(mealPostId);
		for (MealPostIngredient ing : ingredients) {
			mealPostIngredientMapper.insert(ing);
		}

		return ResponseEntity.ok("登録完了");
	}

	@PostMapping("/mealPosts/{mealPostId}/ingredients/update")
	@ResponseBody
	public ResponseEntity<String> updateIngredients(
			@PathVariable Integer mealPostId,
			@RequestParam("nutritionFoodIds") List<Integer> foodIds,
			@RequestParam("amountGrams") List<Double> grams) {
		if (foodIds == null || grams == null || foodIds.size() != grams.size()) {
			return ResponseEntity.badRequest().body("データ不正（サイズ不一致）");
		}

		List<MealPostIngredient> ingredients = new ArrayList<>();

		for (int i = 0; i < foodIds.size(); i++) {
			MealPostIngredient ing = new MealPostIngredient();
			ing.setMealPostId(mealPostId);
			ing.setNutritionFoodId(foodIds.get(i));

			Double gramValue = grams.get(i);
			if (gramValue == null) {
				System.out.println("⚠️ grams[" + i + "] が null です");
				continue;
			}

			Double gram = grams.get(i);
			if (gram != null) {
				ing.setAmountGrams(BigDecimal.valueOf(gram));
			}
			ingredients.add(ing);
		}

		mealPostIngredientMapper.deleteByMealPostId(mealPostId);
		for (MealPostIngredient ing : ingredients) {
			mealPostIngredientMapper.insert(ing);
		}

		return ResponseEntity.ok("食材情報を保存しました");
	}
}
