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
public class NoticeAttachedFileDTO {
	private int fileId;
    private int noticeId;
    private String filePath;

    
    public NoticeAttachedFileDTO(int fileId, int noticeId, String filePath) {
        this.fileId = fileId;
        this.noticeId = noticeId;
        this.filePath = filePath;
    }
}
