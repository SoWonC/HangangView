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
public class DroughtForecastAttachedFileDTO {
	private int fileId;
    private int droughtForecastId;
    private String filePath;

    
    public DroughtForecastAttachedFileDTO(int fileId, int droughtForecastId, String filePath) {
        this.fileId = fileId;
        this.droughtForecastId = droughtForecastId;
        this.filePath = filePath;
    }
}
