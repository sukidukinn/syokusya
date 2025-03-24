package com.example.app.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class BookingFormItem {
	// 代表者氏名
	private String name;
	// メールアドレス
	private String email;
	// 利用人数
	private Integer number;
	// 利用する部屋
	private RoomType roomType;

	//利用予定日
	private LocalDate date;
	// 利用時間帯
	private List<Integer> time;
	// 駐車場利用の有無
	private Integer parking;
	// 施設利用規約への同意
	private Boolean agreement;
}