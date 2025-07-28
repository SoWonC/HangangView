package dto;

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
public class WaterRequestAttachedFileDTO {
	private int fileId;
    private int waterRequestId;
    private String filePath;
    
    public WaterRequestAttachedFileDTO(int fileId, int waterRequestId, String filePath) {
        this.fileId = fileId;
        this.waterRequestId = waterRequestId;
        this.filePath = filePath;
    }

}
