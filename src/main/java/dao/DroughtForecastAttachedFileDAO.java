package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import dto.DroughtForecastAttachedFileDTO;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class DroughtForecastAttachedFileDAO {
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DroughtForecastAttachedFileDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final String SELECT_FILES_BY_DROUGHTFORECAST_ID = "SELECT * FROM droughtforecast_attached_files WHERE droughtforecast_id = ?";
    private static final String DELETE_FILES_BY_DROUGHTFORECAST_ID = "DELETE FROM droughtforecast_attached_files WHERE droughtforecast_id = ?";
    private static final String DELETE_FILE_BY_ID = "DELETE FROM droughtforecast_attached_files WHERE file_id = ?";

    public List<DroughtForecastAttachedFileDTO> getFilesByDroughtForecastId(int droughtForecastId) {
        return jdbcTemplate.query(SELECT_FILES_BY_DROUGHTFORECAST_ID, new Object[]{droughtForecastId}, (rs, rowNum) -> new DroughtForecastAttachedFileDTO(
                rs.getInt("file_id"),
                droughtForecastId,
                rs.getString("file_path")
        ));
    }
    
    public void deleteFilesByDroughtForecastId(int droughtForecastId) {
        jdbcTemplate.update(DELETE_FILES_BY_DROUGHTFORECAST_ID, droughtForecastId);
    }
    
    public void deleteFileById(int fileId) {
        jdbcTemplate.update(DELETE_FILE_BY_ID, fileId);
    }
}
