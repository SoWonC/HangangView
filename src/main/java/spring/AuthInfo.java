package spring;

import lombok.Getter;

@Getter
public class AuthInfo {

	private int id;
	private String email;
	private String name;
	
	public AuthInfo(String email, String name) {
		this.email = email;
		this.name = name;
	}
	
}
