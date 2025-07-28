package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import dto.WaterViewAttachedFileDTO;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class WaterViewAttachedFileDAO {
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WaterViewAttachedFileDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final String SELECT_FILES_BY_WATERVIEW_ID = "SELECT * FROM waterview_attached_files WHERE waterview_id = ?";
    private static final String DELETE_FILES_BY_WATERVIEW_ID = "DELETE FROM waterview_attached_files WHERE waterview_id = ?";
    private static final String DELETE_FILE_BY_ID = "DELETE FROM waterview_attached_files WHERE file_id = ?";

    public List<WaterViewAttachedFileDTO> getFilesByWaterViewId(int waterViewId) {
        return jdbcTemplate.query(SELECT_FILES_BY_WATERVIEW_ID, new Object[]{waterViewId}, (rs, rowNum) -> new WaterViewAttachedFileDTO(
                rs.getInt("file_id"),
                waterViewId,
                rs.getString("file_path")
        ));
    }
    
    public void deleteFilesByWaterViewId(int waterViewId) {
        jdbcTemplate.update(DELETE_FILES_BY_WATERVIEW_ID, waterViewId);
    }
    
    public void deleteFileById(int fileId) {
        jdbcTemplate.update(DELETE_FILE_BY_ID, fileId);
    }
}
