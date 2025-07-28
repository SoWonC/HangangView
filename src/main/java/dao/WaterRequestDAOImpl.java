package dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;


import dto.WaterRequestAttachedFileDTO;
import dto.WaterRequestDTO;

@Repository
public class WaterRequestDAOImpl implements WaterRequestDAO {
	
	 @Autowired
	    public JdbcTemplate jdbcTemplate;
	    
	    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	    }

	    @Override
	    public List<WaterRequestDTO> getAllWaterRequests() {
	        String sql = "SELECT * FROM waterRequest";
	        return jdbcTemplate.query(sql, new WaterRequestRowMapper());
	    }
	    
	    @Override
	    public List<WaterRequestDTO> getWaterRequestsPage(int pageNumber, int pageSize) {
	        int offset = (pageNumber - 1) * pageSize;
	        String sql = "SELECT * FROM waterRequest ORDER BY id DESC LIMIT ? OFFSET ?";
	        return jdbcTemplate.query(sql, new Object[]{pageSize, offset}, new WaterRequestRowMapper());
	    }
	    
	    @Override
	    public int getWaterRequestCount() {
	        String sql = "SELECT COUNT(*) FROM waterRequest";
	        return jdbcTemplate.queryForObject(sql, Integer.class);
	    }

	    @Override
	    public WaterRequestDTO getWaterRequestById(Long id) {
	        String sql = "SELECT * FROM waterRequest WHERE id = ?";
	        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new WaterRequestRowMapper());
	    }

	    @Override
	    public int addWaterRequest(WaterRequestDTO waterRequest, List<MultipartFile> files) {
	        String sql = "INSERT INTO waterRequest (name, applicant, tel, email, fax, river, message) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        KeyHolder keyHolder = new GeneratedKeyHolder();
	        jdbcTemplate.update(connection -> {
	            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
	            ps.setString(1, waterRequest.getName());
	            ps.setString(2, waterRequest.getApplicant());
	            ps.setString(3, waterRequest.getTel());
	            ps.setString(4, waterRequest.getEmail());
	            ps.setString(5, waterRequest.getFax());
	            ps.setString(6, waterRequest.getRiver());
	            ps.setString(7, waterRequest.getMessage());
	            return ps;
	        }, keyHolder);

	        Number key = keyHolder.getKey();
	        if (key != null) {
	            long generatedId = key.longValue();
	            waterRequest.setId(generatedId);

	            if (files != null) {
	                List<String> savedFilesPaths = new ArrayList<>();
	                for (MultipartFile file : files) {
	                    if (!file.isEmpty()) {
	                        String filePath = saveFileOnServer(file);
	                        addAttachment(generatedId, filePath);
	                        savedFilesPaths.add(filePath);
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
	    public int updateWaterRequest(WaterRequestDTO waterRequest, List<MultipartFile> newFiles) {
	        String sql = "UPDATE waterRequest SET name = ?, applicant = ?, tel = ?, email = ?, fax = ?, river = ?, message = ? WHERE id = ?";
	        int updateCount = jdbcTemplate.update(sql, waterRequest.getName(), waterRequest.getApplicant(), waterRequest.getTel(), waterRequest.getEmail(), waterRequest.getFax(), waterRequest.getRiver(), waterRequest.getMessage(), waterRequest.getId());
	        if (newFiles != null && !newFiles.isEmpty()) {
	            for (MultipartFile file : newFiles) {
	                if (!file.isEmpty()) {
	                    String filePath = saveFileOnServer(file);
	                    addAttachment(waterRequest.getId(), filePath);
	                }
	            }
	        }
	        return updateCount;
	    }

	    @Override
	    public int deleteWaterRequest(Long id) {
	        String sql = "DELETE FROM waterRequest WHERE id = ?";
	        return jdbcTemplate.update(sql, id);
	    }

	    private List<String> getAttachmentsByWaterRequestId(Long waterRequestId) {
	        String sql = "SELECT file_path FROM waterRequest_attached_files WHERE waterRequest_id = ?";
	        return jdbcTemplate.queryForList(sql, new Object[]{waterRequestId}, String.class);
	    }

	    public void addAttachment(Long waterRequestId, String filePath) {
	        String fileName = new File(filePath).getName();
	        String sqlCheck = "SELECT COUNT(*) FROM waterRequest_attached_files WHERE waterRequest_id = ? AND file_path LIKE ?";
	        int count = jdbcTemplate.queryForObject(sqlCheck, new Object[]{waterRequestId, "%" + fileName}, Integer.class);
	        if (count == 0) {
	            String sql = "INSERT INTO waterRequest_attached_files (waterRequest_id, file_path) VALUES (?, ?)";
	            jdbcTemplate.update(sql, waterRequestId, filePath);
	        }
	    }

	    private class WaterRequestRowMapper implements RowMapper<WaterRequestDTO> {
	        @Override
	        public WaterRequestDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	            WaterRequestDTO waterRequest = new WaterRequestDTO();
	            waterRequest.setId(rs.getLong("id"));
	            waterRequest.setName(rs.getString("name"));
	            waterRequest.setApplicant(rs.getString("applicant"));
	            waterRequest.setTel(rs.getString("tel"));
	            waterRequest.setEmail(rs.getString("email"));
	            waterRequest.setFax(rs.getString("fax"));
	            waterRequest.setRiver(rs.getString("river"));
	            waterRequest.setMessage(rs.getString("message"));
	            waterRequest.setRequestDate(rs.getTimestamp("requestDate"));
	            
	            List<String> attachedFiles = getAttachmentsByWaterRequestId(rs.getLong("id"));
	            return waterRequest;
	        }
	    }

	    @Override
	    public List<WaterRequestDTO> searchWaterRequests(String searchType, String searchTerm, int pageNumber, int pageSize) {
	        int offset = (pageNumber - 1) * pageSize;
	        String sql;
	        switch (searchType) {
	            case "title":
	                sql = "SELECT * FROM waterRequest WHERE name LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
	                break;
	            case "content":
	                sql = "SELECT * FROM waterRequest WHERE message LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
	                break;
	            default:
	                throw new IllegalArgumentException("Invalid search type");
	        }
	        return jdbcTemplate.query(sql, new Object[]{"%" + searchTerm + "%", pageSize, offset}, new WaterRequestRowMapper());
	    }

	    @Override
	    public int getSearchWaterRequestCount(String searchType, String searchTerm) {
	        String sql;
	        switch (searchType) {
	            case "name":
	                sql = "SELECT COUNT(*) FROM waterRequest WHERE name LIKE ?";
	                break;
	            case "message":
	                sql = "SELECT COUNT(*) FROM waterRequest WHERE message LIKE ?";
	                break;
	            default:
	                throw new IllegalArgumentException("Invalid search type");
	        }
	        return jdbcTemplate.queryForObject(sql, new Object[]{"%" + searchTerm + "%"}, Integer.class);
	    }
	    
	    

	    @Override
	    public void deleteFilesByWaterRequestId(int waterRequestId) {
	        List<String> attachedFiles = getAttachmentsByWaterRequestId((long) waterRequestId);
	        for (String filePath : attachedFiles) {
	            deleteFileFromStorage(filePath);
	        }
	        String deleteFilesSql = "DELETE FROM waterRequest_attached_files WHERE waterRequest_id = ?";
	        jdbcTemplate.update(deleteFilesSql, waterRequestId);
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
	    public List<WaterRequestAttachedFileDTO> getAttachedFilesByWaterRequestId(Long waterRequestId) {
	        String sql = "SELECT * FROM waterRequest_attached_files WHERE waterRequest_id = ?";
	        return jdbcTemplate.query(sql, new Object[]{waterRequestId}, new WaterRequestAttachedFileRowMapper());
	    }
	    
	    public class WaterRequestAttachedFileRowMapper implements RowMapper<WaterRequestAttachedFileDTO> {
	        @Override
	        public WaterRequestAttachedFileDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
	            WaterRequestAttachedFileDTO attachedFile = new WaterRequestAttachedFileDTO();
	            attachedFile.setFileId(rs.getInt("fileId"));
	            attachedFile.setWaterRequestId(rs.getInt("waterRequestId"));
	            attachedFile.setFilePath(rs.getString("filePath"));
	            return attachedFile;
	        }
	    }
    
	    @Override
	    public void deleteFileById(int fileId) {
	        
	        String sqlSelectPath = "SELECT file_path FROM waterRequest_attached_files WHERE file_id = ?";
	        String filePath = jdbcTemplate.queryForObject(sqlSelectPath, new Object[]{fileId}, String.class);

	        
	        deleteFileFromStorage(filePath);

	        
	        String sqlDelete = "DELETE FROM waterRequest_attached_files WHERE file_id = ?";
	        jdbcTemplate.update(sqlDelete, fileId);
	    }

    
    
   }

