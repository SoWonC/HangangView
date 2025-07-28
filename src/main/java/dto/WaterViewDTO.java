package dto;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WaterViewDTO {
    
	 private Long id;  
	    private String title;  
	    private String department;  
	    private String personInCharge;  
	    private String phoneNumber;  
	    private String content;  
	    private Timestamp registerDate;  
	    private int viewCount;  
	    private List<MultipartFile> attachedFiles;  

	    
	    public void setAttachedFiles(List<MultipartFile> attachedFiles) {
	        this.attachedFiles = attachedFiles;
	    }
	    
	    
	    public List<MultipartFile> getAttachedFiles() {
	        return attachedFiles;
	    }
	    
	}