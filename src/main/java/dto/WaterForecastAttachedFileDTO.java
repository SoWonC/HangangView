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
public class WaterForecastAttachedFileDTO {
	private int fileId;
    private int waterForecastId;
    private String filePath;

    
    public WaterForecastAttachedFileDTO(int fileId, int waterForecastId, String filePath) {
        this.fileId = fileId;
        this.waterForecastId = waterForecastId;
        this.filePath = filePath;
    }
}
