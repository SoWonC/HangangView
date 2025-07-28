package dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import dto.NoticeAttachedFileDTO;
import javax.sql.DataSource;

@Repository
public class NoticeAttachedFileDAO {
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public NoticeAttachedFileDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final String SELECT_FILES_BY_NOTICE_ID = "SELECT * FROM notice_attached_files WHERE notice_id = ?";
    private static final String DELETE_FILES_BY_NOTICE_ID = "DELETE FROM notice_attached_files WHERE notice_id = ?";
    private static final String DELETE_FILE_BY_ID = "DELETE FROM notice_attached_files WHERE file_id = ?";

    public List<NoticeAttachedFileDTO> getFilesByNoticeId(int noticeId) {
        return jdbcTemplate.query(SELECT_FILES_BY_NOTICE_ID, new Object[]{noticeId}, (rs, rowNum) -> new NoticeAttachedFileDTO(
                rs.getInt("file_id"),
                noticeId,
                rs.getString("file_path")
        ));
    }
    
    public void deleteFilesByNoticeId(int noticeId) {
        jdbcTemplate.update(DELETE_FILES_BY_NOTICE_ID, noticeId);
    }
    
    public void deleteFileById(int fileId) {
        jdbcTemplate.update(DELETE_FILE_BY_ID, fileId);
    }
}
