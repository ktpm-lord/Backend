package com.fitlife.app.DTO.Request;

import lombok.Data;

@Data
public class RegistrationRequest {
	private String fullName;
	private String username;
	private String password;
	private int age;
	private String gender;
}
