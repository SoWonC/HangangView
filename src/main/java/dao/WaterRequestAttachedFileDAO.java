package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import dto.WaterRequestAttachedFileDTO;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class WaterRequestAttachedFileDAO {
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WaterRequestAttachedFileDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final String SELECT_FILES_BY_WATERREQUEST_ID = "SELECT * FROM waterRequest_attached_files WHERE waterRequest_id = ?";
    private static final String DELETE_FILES_BY_WATERREQUEST_ID = "DELETE FROM waterRequest_attached_files WHERE waterRequest_id = ?";
    private static final String DELETE_FILE_BY_ID = "DELETE FROM waterRequest_attached_files WHERE file_id = ?";

    public List<WaterRequestAttachedFileDTO> getFilesByWaterRequestId(int waterRequestId) {
        return jdbcTemplate.query(SELECT_FILES_BY_WATERREQUEST_ID, new Object[]{waterRequestId}, (rs, rowNum) -> new WaterRequestAttachedFileDTO(
                rs.getInt("file_id"),
                waterRequestId,
                rs.getString("file_path")
        ));
    }
    
    public void deleteFilesByWaterRequestId(int waterRequestId) {
        jdbcTemplate.update(DELETE_FILES_BY_WATERREQUEST_ID, waterRequestId);
    }
    
    public void deleteFileById(int fileId) {
        jdbcTemplate.update(DELETE_FILE_BY_ID, fileId);
    }
}
