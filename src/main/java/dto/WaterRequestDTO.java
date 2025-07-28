package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WaterRequestDTO {
	private Long id;
	private String name;
	private String applicant;
	private String tel;
	private String email;
	private String fax;
	private String river;
	private String message;
	private Timestamp requestDate;
	private List<MultipartFile> attachedFiles;
	
	public void setAttachedFiles(List<MultipartFile> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}
	
	public List<MultipartFile> getAttachedFiles() {
		return attachedFiles;
	}
	
	
	

}
