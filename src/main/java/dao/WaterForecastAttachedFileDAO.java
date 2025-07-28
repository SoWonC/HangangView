package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import dto.WaterForecastAttachedFileDTO;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class WaterForecastAttachedFileDAO {
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WaterForecastAttachedFileDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final String SELECT_FILES_BY_WATERFORECAST_ID = "SELECT * FROM waterforecast_attached_files WHERE waterforecast_id = ?";
    private static final String DELETE_FILES_BY_WATERFORECAST_ID = "DELETE FROM waterforecast_attached_files WHERE waterforecast_id = ?";
    private static final String DELETE_FILE_BY_ID = "DELETE FROM waterforecast_attached_files WHERE file_id = ?";

    public List<WaterForecastAttachedFileDTO> getFilesByWaterForecastId(int waterForecastId) {
        return jdbcTemplate.query(SELECT_FILES_BY_WATERFORECAST_ID, new Object[]{waterForecastId}, (rs, rowNum) -> new WaterForecastAttachedFileDTO(
                rs.getInt("file_id"),
                waterForecastId,
                rs.getString("file_path")
        ));
    }
    
    public void deleteFilesByWaterForecastId(int waterForecastId) {
        jdbcTemplate.update(DELETE_FILES_BY_WATERFORECAST_ID, waterForecastId);
    }
    
    public void deleteFileById(int fileId) {
        jdbcTemplate.update(DELETE_FILE_BY_ID, fileId);
    }
}
