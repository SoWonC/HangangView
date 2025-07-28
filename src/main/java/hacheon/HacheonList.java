package hacheon;

import lombok.Getter;
import lombok.ToString;
@Getter
@ToString
public class HacheonList {

	private String hacheon_name;
	private String hacheon_code;
	private String hacheon_grade;
	
	
	

	public HacheonList(String hacheon_name, String hacheon_code, String hacheon_grade) {
		this.hacheon_name=hacheon_name;
		this.hacheon_code=hacheon_code;
		this.hacheon_grade=hacheon_grade;
	}

}
