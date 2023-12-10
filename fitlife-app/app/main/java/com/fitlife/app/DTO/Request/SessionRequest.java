package com.fitlife.app.DTO.Request;

import lombok.Data;

@Data
public class SessionRequest {
	private Long dailyWorkouts;

	private Boolean startWithBoot;
	private Boolean randomMix;
	private int timePerLesson;
	private int transferTime;
	private String description;
	private String name;
	private String level;


}
