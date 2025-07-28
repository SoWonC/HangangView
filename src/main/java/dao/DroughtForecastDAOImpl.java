package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import dto.DroughtForecastAttachedFileDTO;
import dto.DroughtForecastDTO;

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
public class DroughtForecastDAOImpl implements DroughtForecastDAO {

    @Autowired
    public JdbcTemplate jdbcTemplate;
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<DroughtForecastDTO> getAllDroughtForecasts() {
        String sql = "SELECT * FROM droughtforecast";
        return jdbcTemplate.query(sql, new DroughtForecastRowMapper());
    }
    
    @Override
    public List<DroughtForecastDTO> getDroughtForecastsPage(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql = "SELECT * FROM droughtforecast ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{pageSize, offset}, new DroughtForecastRowMapper());
    }
    
    @Override
    public int getDroughtForecastCount() {
        String sql = "SELECT COUNT(*) FROM droughtforecast";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public DroughtForecastDTO getDroughtForecastById(Long id) {
        String sql = "SELECT * FROM droughtforecast WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new DroughtForecastRowMapper());
    }

    @Override
    public int addDroughtForecast(DroughtForecastDTO droughtForecast, List<MultipartFile> files) {
        String sql = "INSERT INTO droughtforecast (title, department, personInCharge, phoneNumber, content, viewCount) VALUES (?, ?, ?, ?, ?, 0)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, droughtForecast.getTitle());
            ps.setString(2, droughtForecast.getDepartment());
            ps.setString(3, droughtForecast.getPersonInCharge());
            ps.setString(4, droughtForecast.getPhoneNumber());
            ps.setString(5, droughtForecast.getContent());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            long generatedId = key.longValue();
            droughtForecast.setId(generatedId);

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
    public int updateDroughtForecast(DroughtForecastDTO droughtForecast, List<MultipartFile> newFiles) {
        String sql = "UPDATE droughtforecast SET title = ?, department = ?, personInCharge = ?, phoneNumber = ?, content = ? WHERE id = ?";
        int updateCount = jdbcTemplate.update(sql, droughtForecast.getTitle(), droughtForecast.getDepartment(), droughtForecast.getPersonInCharge(), droughtForecast.getPhoneNumber(), droughtForecast.getContent(), droughtForecast.getId());
        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String filePath = saveFileOnServer(file);
                    addAttachment(droughtForecast.getId(), filePath);
                }
            }
        }

        return updateCount;
    }

    @Override
    public int deleteDroughtForecast(Long id) {
        String sql = "DELETE FROM droughtforecast WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private List<String> getAttachmentsByDroughtForecastId(Long droughtForecastId) {
        String sql = "SELECT file_path FROM droughtforecast_attached_files WHERE droughtforecast_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{droughtForecastId}, String.class);
    }

    public void addAttachment(Long droughtForecastId, String filePath) {
    	
        String fileName = new File(filePath).getName();

        
        String sqlCheck = "SELECT COUNT(*) FROM droughtForecast_attached_files WHERE droughtForecast_id = ? AND file_path LIKE ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, new Object[]{droughtForecastId, "%" + fileName}, Integer.class);

        
        if (count == 0) {
        String sql = "INSERT INTO droughtForecast_attached_files (droughtForecast_id, file_path) VALUES (?, ?)";
        jdbcTemplate.update(sql, droughtForecastId, filePath);
    }
   }

    private class DroughtForecastRowMapper implements RowMapper<DroughtForecastDTO> {
        @Override
        public DroughtForecastDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            DroughtForecastDTO droughtForecast = new DroughtForecastDTO();
            droughtForecast.setId(rs.getLong("id"));
            droughtForecast.setTitle(rs.getString("title"));
            droughtForecast.setDepartment(rs.getString("department"));
            droughtForecast.setPersonInCharge(rs.getString("personInCharge"));
            droughtForecast.setPhoneNumber(rs.getString("phoneNumber"));
            droughtForecast.setContent(rs.getString("content"));
            droughtForecast.setRegisterDate(rs.getTimestamp("registerDate"));
            droughtForecast.setViewCount(rs.getInt("viewCount"));
            List<String> attachedFiles = getAttachmentsByDroughtForecastId(rs.getLong("id"));

            return droughtForecast;
        }
    }

    @Override
    public List<DroughtForecastDTO> searchDroughtForecasts(String searchType, String searchTerm, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql;

        switch (searchType) {
            case "title":
                sql = "SELECT * FROM droughtforecast WHERE title LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
                break;
            case "content":
                sql = "SELECT * FROM droughtforecast WHERE content LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }

        return jdbcTemplate.query(
            sql,
            new Object[]{"%" + searchTerm + "%", pageSize, offset},
            new DroughtForecastRowMapper()
        );
    }

    @Override
    public int getSearchDroughtForecastCount(String searchType, String searchTerm) {
        String sql;

        switch (searchType) {
            case "title":
                sql = "SELECT COUNT(*) FROM droughtforecast WHERE title LIKE ?";
                break;
            case "content":
                sql = "SELECT COUNT(*) FROM droughtforecast WHERE content LIKE ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }

        return jdbcTemplate.queryForObject(sql, new Object[]{"%" + searchTerm + "%"}, Integer.class);
    }

    @Override
    public void increaseViewCount(Long id) {
        String sql = "UPDATE droughtforecast SET viewCount = viewCount + 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteFilesByDroughtForecastId(int droughtForecastId) {
        List<String> attachedFiles = getAttachmentsByDroughtForecastId((long) droughtForecastId);
        for (String filePath : attachedFiles) {
            deleteFileFromStorage(filePath);
        }
        String deleteFilesSql = "DELETE FROM droughtforecast_attached_files WHERE droughtforecast_id = ?";
        jdbcTemplate.update(deleteFilesSql, droughtForecastId);
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
    public List<DroughtForecastAttachedFileDTO> getAttachedFilesByDroughtForecastId(Long droughtForecastId) {
        String sql = "SELECT * FROM droughtforecast_attached_files WHERE droughtforecast_id = ?";
        return jdbcTemplate.query(sql, new Object[]{droughtForecastId}, new DroughtForecastAttachedFileRowMapper());
    }

    public class DroughtForecastAttachedFileRowMapper implements RowMapper<DroughtForecastAttachedFileDTO> {
        @Override
        public DroughtForecastAttachedFileDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            DroughtForecastAttachedFileDTO attachedFile = new DroughtForecastAttachedFileDTO();
            attachedFile.setFileId(rs.getInt("fileId"));
            attachedFile.setDroughtForecastId(rs.getInt("droughtForecastId"));
            attachedFile.setFilePath(rs.getString("filePath"));
            return attachedFile;
        }
    }

    @Override
    public void deleteFileById(int fileId) {
        String sqlSelectPath = "SELECT file_path FROM droughtforecast_attached_files WHERE file_id = ?";
        String filePath = jdbcTemplate.queryForObject(sqlSelectPath, new Object[]{fileId}, String.class);
        deleteFileFromStorage(filePath);
        String sqlDelete = "DELETE FROM droughtforecast_attached_files WHERE file_id = ?";
        jdbcTemplate.update(sqlDelete, fileId);
    }

    
    
	}

