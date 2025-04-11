package com.example.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.app.domain.BookingFormItem;
import com.example.app.domain.RoomType;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/booking")
public class BookingController {
	@GetMapping
	public String bookingGet(Model model) {
		var bookingFormItem = new BookingFormItem();
		// あらかじめ「駐車場を利用しない」を設定
		bookingFormItem.setParking(2);
		model.addAttribute("bookingFormItem", bookingFormItem);
		// 部屋の種類のリスト
		model.addAttribute("roomTypeList", getRoomTypeList());
		return "booking";
	}

	@PostMapping
	public String bookingPost(
			@Valid BookingFormItem bookingFormItem,
			Errors errors,
			Model model) {
		// バリデーションエラーの場合、フォームを再表示
		if (errors.hasErrors()) {
			// エラー内容の捕捉
			List<ObjectError> objList = errors.getAllErrors();
			for (ObjectError obj : objList) {
				System.out.println(obj.toString());
			}
			model.addAttribute("roomTypeList", getRoomTypeList());
			return "booking";
		}
		// 完了画面の表示
		return "bookingDone";
	}

	// 部屋の種類のリストを返すメソッド
	private List<RoomType> getRoomTypeList() {
		List<RoomType> roomTypeList = new ArrayList<>();
		roomTypeList.add(new RoomType(1, "会議室 A", "ホワイトボード有り"));
		roomTypeList.add(new RoomType(2, "会議室 B", "ホワイトボード無し"));
		roomTypeList.add(new RoomType(3, "視聴覚室", "プロジェクター有り"));
		roomTypeList.add(new RoomType(4, "多目的ルーム", "フローリング"));
		return roomTypeList;
	}
}
