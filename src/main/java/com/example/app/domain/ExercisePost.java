package com.example.app.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercisePost {

	private Integer id;
	private Integer userId;

	@NotBlank(message = "運動名を入力してください")
	private String exerciseName;

	private String bodyPart; // 腕、足、全身など

	@Min(value = 1, message = "セット数は1以上で入力してください")
	@Max(value = 100, message = "セット数は100以下で入力してください")
	private Integer setCount;

	@Min(value = 1, message = "回数は1以上で入力してください")
	@Max(value = 1000, message = "回数は1000以下で入力してください")
	private Integer repetitionCount;

	@Min(value = 1, message = "運動時間は1分以上で入力してください")
	@Max(value = 300, message = "運動時間は300分以下で入力してください")
	private Integer durationMinutes;

	private String intensityLevel; // 強度（例：高、中、低）

	@Min(value = 1, message = "消費カロリーは1以上で入力してください")
	@Max(value = 3000, message = "消費カロリーは3000以下で入力してください")
	private Integer calorieBurned;

	private String photoPath;

	private String memo;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime exerciseTime;

	private LocalDateTime created;

	private LocalDateTime updated;

	private Boolean deleted;
}
