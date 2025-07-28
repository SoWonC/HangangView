package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import dto.WaterViewAttachedFileDTO;
import dto.WaterViewDTO;

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
public class WaterViewDAOImpl implements WaterViewDAO {

    @Autowired
    public JdbcTemplate jdbcTemplate;
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<WaterViewDTO> getAllWaterViews() {
        String sql = "SELECT * FROM waterview";
        return jdbcTemplate.query(sql, new WaterViewRowMapper());
    }
    
    @Override
    public List<WaterViewDTO> getWaterViewsPage(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql = "SELECT * FROM waterview ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{pageSize, offset}, new WaterViewRowMapper());
    }
    
    @Override
    public int getWaterViewCount() {
        String sql = "SELECT COUNT(*) FROM waterview";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public WaterViewDTO getWaterViewById(Long id) {
        String sql = "SELECT * FROM waterview WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new WaterViewRowMapper());
    }

    @Override
    public int addWaterView(WaterViewDTO waterView, List<MultipartFile> files) {
        
        String sql = "INSERT INTO waterview (title, department, personInCharge, phoneNumber, content, viewCount) VALUES (?, ?, ?, ?, ?, 0)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, waterView.getTitle());
            ps.setString(2, waterView.getDepartment());
            ps.setString(3, waterView.getPersonInCharge());
            ps.setString(4, waterView.getPhoneNumber());
            ps.setString(5, waterView.getContent());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            long generatedId = key.longValue();
            waterView.setId(generatedId);

           
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
    public int updateWaterView(WaterViewDTO waterView, List<MultipartFile> newFiles) {
        String sql = "UPDATE waterview SET title = ?, department = ?, personInCharge = ?, phoneNumber = ?, content = ? WHERE id = ?";
        int updateCount = jdbcTemplate.update(sql, waterView.getTitle(), waterView.getDepartment(), waterView.getPersonInCharge(), waterView.getPhoneNumber(), waterView.getContent(), waterView.getId());
        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String filePath = saveFileOnServer(file);
                    addAttachment(waterView.getId(), filePath);
                }
            }
        }

        return updateCount;
    }

    @Override
    public int deleteWaterView(Long id) {
        String sql = "DELETE FROM waterview WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private List<String> getAttachmentsByWaterViewId(Long waterViewId) {
        String sql = "SELECT file_path FROM waterview_attached_files WHERE waterview_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{waterViewId}, String.class);
    }

    public void addAttachment(Long waterViewId, String filePath) {
        
        String fileName = new File(filePath).getName();

        
        String sqlCheck = "SELECT COUNT(*) FROM waterview_attached_files WHERE waterview_id = ? AND file_path LIKE ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, new Object[]{waterViewId, "%" + fileName}, Integer.class);

        
        if (count == 0) {
            String sql = "INSERT INTO waterview_attached_files (waterview_id, file_path) VALUES (?, ?)";
            jdbcTemplate.update(sql, waterViewId, filePath);
        }
    }

    private class WaterViewRowMapper implements RowMapper<WaterViewDTO> {
        @Override
        public WaterViewDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            WaterViewDTO waterView = new WaterViewDTO();
            waterView.setId(rs.getLong("id"));
            waterView.setTitle(rs.getString("title"));
            waterView.setDepartment(rs.getString("department"));
            waterView.setPersonInCharge(rs.getString("personInCharge"));
            waterView.setPhoneNumber(rs.getString("phoneNumber"));
            waterView.setContent(rs.getString("content"));
            waterView.setRegisterDate(rs.getTimestamp("registerDate"));
            waterView.setViewCount(rs.getInt("viewCount"));
            List<String> attachedFiles = getAttachmentsByWaterViewId(rs.getLong("id"));

            return waterView;
        }
    }

    @Override
    public List<WaterViewDTO> searchWaterViews(String searchType, String searchTerm, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql;
        switch (searchType) {
            case "title":
                sql = "SELECT * FROM waterview WHERE title LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
                break;
            case "content":
                sql = "SELECT * FROM waterview WHERE content LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }

        return jdbcTemplate.query(sql, new Object[]{"%" + searchTerm + "%", pageSize, offset}, new WaterViewRowMapper());
    }

    @Override
    public int getSearchWaterViewCount(String searchType, String searchTerm) {
        String sql;
        switch (searchType) {
            case "title":
                sql = "SELECT COUNT(*) FROM waterview WHERE title LIKE ?";
                break;
            case "content":
                sql = "SELECT COUNT(*) FROM waterview WHERE content LIKE ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }

        return jdbcTemplate.queryForObject(sql, new Object[]{"%" + searchTerm + "%"}, Integer.class);
    }

    @Override
    public void increaseViewCount(Long id) {
        String sql = "UPDATE waterview SET viewCount = viewCount + 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteFilesByWaterViewId(int waterViewId) {
       
        List<String> attachedFiles = getAttachmentsByWaterViewId((long) waterViewId);

        
        for (String filePath : attachedFiles) {
            deleteFileFromStorage(filePath);
        }

       
        String deleteFilesSql = "DELETE FROM waterview_attached_files WHERE waterview_id = ?";
        jdbcTemplate.update(deleteFilesSql, waterViewId);
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
    public List<WaterViewAttachedFileDTO> getAttachedFilesByWaterViewId(Long waterViewId) {
       
        String sql = "SELECT * FROM waterview_attached_files WHERE waterview_id = ?";
        return jdbcTemplate.query(sql, new Object[]{waterViewId}, new WaterViewAttachedFileRowMapper());
    }

    public class WaterViewAttachedFileRowMapper implements RowMapper<WaterViewAttachedFileDTO> {
        @Override
        public WaterViewAttachedFileDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            WaterViewAttachedFileDTO attachedFile = new WaterViewAttachedFileDTO();
            attachedFile.setFileId(rs.getInt("fileId"));
            attachedFile.setWaterViewId(rs.getInt("waterviewId"));
            attachedFile.setFilePath(rs.getString("filePath"));
            return attachedFile;
        }
    }

    @Override
    public void deleteFileById(int fileId) {
       
        String sqlSelectPath = "SELECT file_path FROM waterview_attached_files WHERE file_id = ?";
        String filePath = jdbcTemplate.queryForObject(sqlSelectPath, new Object[]{fileId}, String.class);

        
        deleteFileFromStorage(filePath);

        
        String sqlDelete = "DELETE FROM waterview_attached_files WHERE file_id = ?";
        jdbcTemplate.update(sqlDelete, fileId);
    }

    
    
	}

