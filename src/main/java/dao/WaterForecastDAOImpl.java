package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import dto.WaterForecastAttachedFileDTO;
import dto.WaterForecastDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WaterForecastDAOImpl implements WaterForecastDAO {

    @Autowired
    public JdbcTemplate jdbcTemplate;
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<WaterForecastDTO> getAllWaterForecasts() {
        String sql = "SELECT * FROM waterforecast";
        return jdbcTemplate.query(sql, new WaterForecastRowMapper());
    }
    
    @Override
    public List<WaterForecastDTO> getWaterForecastsPage(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql = "SELECT * FROM waterforecast ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{pageSize, offset}, new WaterForecastRowMapper());
    }
    
    @Override
    public int getWaterForecastCount() {
        String sql = "SELECT COUNT(*) FROM waterforecast";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public WaterForecastDTO getWaterForecastById(Long id) {
        String sql = "SELECT * FROM waterforecast WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new WaterForecastRowMapper());
    }

    @Override
    public int addWaterForecast(WaterForecastDTO waterForecast, List<MultipartFile> files) {
        
        String sql = "INSERT INTO waterforecast (title, department, personInCharge, phoneNumber, content, viewCount) VALUES (?, ?, ?, ?, ?, 0)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, waterForecast.getTitle());
            ps.setString(2, waterForecast.getDepartment());
            ps.setString(3, waterForecast.getPersonInCharge());
            ps.setString(4, waterForecast.getPhoneNumber());
            ps.setString(5, waterForecast.getContent());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            long generatedId = key.longValue();
            waterForecast.setId(generatedId);

            
            if (files != null) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        String filePath = saveFileOnServer(file);
                        addAttachment(generatedId, filePath);
                    }
                }
            }

            return 1;
        } else {
            return 0;
        }
    }

    public String saveFileOnServer(MultipartFile file) {
        if (file.isEmpty()) {
            
            throw new RuntimeException("Failed to store empty file " + file.getOriginalFilename());
        }
        try {
            String uploadDir = "C:/hangangsave/"; 
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
           
            String fileName = file.getOriginalFilename();
            Path destinationFile = Paths.get(uploadDir).resolve(Paths.get(fileName)).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            

            return fileName;

        } catch (IOException e) {
           
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

   

    @Override
    public int updateWaterForecast(WaterForecastDTO waterForecast, List<MultipartFile> newFiles) {
        String sql = "UPDATE waterForecast SET title = ?, department = ?, personInCharge = ?, phoneNumber = ?, content = ? WHERE id = ?";
        int updateCount = jdbcTemplate.update(sql, waterForecast.getTitle(), waterForecast.getDepartment(), waterForecast.getPersonInCharge(), waterForecast.getPhoneNumber(), waterForecast.getContent(), waterForecast.getId());
        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String filePath = saveFileOnServer(file);
                    addAttachment(waterForecast.getId(), filePath);
                }
            }
        }

        return updateCount;
    }

    @Override
    public int deleteWaterForecast(Long id) {
        String sql = "DELETE FROM waterforecast WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private List<String> getAttachmentsByWaterForecastId(Long waterForecastId) {
        String sql = "SELECT file_path FROM waterForecast_attached_files WHERE waterForecast_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{waterForecastId}, String.class);
    }

    public void addAttachment(Long waterForecastId, String filePath) {
    	
        String fileName = new File(filePath).getName();

       
        String sqlCheck = "SELECT COUNT(*) FROM waterForecast_attached_files WHERE waterForecast_id = ? AND file_path LIKE ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, new Object[]{waterForecastId, "%" + fileName}, Integer.class);

        
        if (count == 0) {
        String sql = "INSERT INTO waterForecast_attached_files (waterForecast_id, file_path) VALUES (?, ?)";
        jdbcTemplate.update(sql, waterForecastId, filePath);
    }
   }

    private class WaterForecastRowMapper implements RowMapper<WaterForecastDTO> {
        @Override
        public WaterForecastDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	WaterForecastDTO waterForecast = new WaterForecastDTO();
            waterForecast.setId(rs.getLong("id"));
            waterForecast.setTitle(rs.getString("title"));
            waterForecast.setDepartment(rs.getString("department"));
            waterForecast.setPersonInCharge(rs.getString("personInCharge"));
            waterForecast.setPhoneNumber(rs.getString("phoneNumber"));
            waterForecast.setContent(rs.getString("content"));
            waterForecast.setRegisterDate(rs.getTimestamp("registerDate"));
            waterForecast.setViewCount(rs.getInt("viewCount")); // 조회수를 설정하는 코드 추가
            List<String> attachedFiles = getAttachmentsByWaterForecastId(rs.getLong("id"));

            return waterForecast;
        }
    }
    

    
    @Override
    public List<WaterForecastDTO> searchWaterForecasts(String searchType, String searchTerm, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql;

        switch (searchType) {
            case "title":
                sql = "SELECT * FROM waterforecast WHERE title LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
                break;
            case "content":
                sql = "SELECT * FROM waterforecast WHERE content LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }

        return jdbcTemplate.query(
            sql,
            new Object[]{"%" + searchTerm + "%", pageSize, offset},
            new WaterForecastRowMapper()
        );
    }

    @Override
    public int getSearchWaterForecastCount(String searchType, String searchTerm) {
        String sql;

        switch (searchType) {
            case "title":
                sql = "SELECT COUNT(*) FROM waterforecast WHERE title LIKE ?";
                break;
            case "content":
                sql = "SELECT COUNT(*) FROM waterforecast WHERE content LIKE ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }

        return jdbcTemplate.queryForObject(sql, new Object[]{"%" + searchTerm + "%"}, Integer.class);
    }
    
    @Override
    public void increaseViewCount(Long id) {
        String sql = "UPDATE waterforecast SET viewCount = viewCount + 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteFilesByWaterForecastId(int waterForecastId) {
        
        List<String> attachedFiles = getAttachmentsByWaterForecastId((long) waterForecastId);

       
        for (String filePath : attachedFiles) {
            deleteFileFromStorage(filePath);
        }

       
        String deleteFilesSql = "DELETE FROM waterForecast_attached_files WHERE waterForecast_id = ?";
        jdbcTemplate.update(deleteFilesSql, waterForecastId);
    }

    private void deleteFileFromStorage(String filePath) {
       
    	
    	File fileToDelete = new File(filePath);
        
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                System.out.println("파일 삭제 성공: " + filePath);
            } else {
                System.err.println("파일 삭제 실패: " + filePath);
            }
        } else {
            System.err.println("파일이 존재하지 않습니다: " + filePath);
        }
    }
    
    @Override
    public List<WaterForecastAttachedFileDTO> getAttachedFilesByWaterForecastId(Long waterForecastId) {
        
        String sql = "SELECT * FROM waterForecast_attached_files WHERE waterForecast_id = ?";
        return jdbcTemplate.query(sql, new Object[]{waterForecastId}, new WaterForecastAttachedFileRowMapper());
    }
    
    public class WaterForecastAttachedFileRowMapper implements RowMapper<WaterForecastAttachedFileDTO> {
        @Override
        public WaterForecastAttachedFileDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
          
            WaterForecastAttachedFileDTO attachedFile = new WaterForecastAttachedFileDTO();
            attachedFile.setFileId(rs.getInt("fileId"));
            attachedFile.setWaterForecastId(rs.getInt("waterForecastId"));
            attachedFile.setFilePath(rs.getString("filePath"));
            return attachedFile;
        }
    }
    
    @Override
    public void deleteFileById(int fileId) {
      
        String sqlSelectPath = "SELECT file_path FROM waterForecast_attached_files WHERE file_id = ?";
        String filePath = jdbcTemplate.queryForObject(sqlSelectPath, new Object[]{fileId}, String.class);

       
        deleteFileFromStorage(filePath);

       
        String sqlDelete = "DELETE FROM waterForecast_attached_files WHERE file_id = ?";
        jdbcTemplate.update(sqlDelete, fileId);
    }

    
    
	}

