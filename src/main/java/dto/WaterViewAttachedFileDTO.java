package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@Data
@ToString
public class WaterViewAttachedFileDTO {
	private int fileId;
    private int waterViewId;
    private String filePath;

    
    public WaterViewAttachedFileDTO(int fileId, int waterViewId, String filePath) {
        this.fileId = fileId;
        this.waterViewId = waterViewId;
        this.filePath = filePath;
    }
}
