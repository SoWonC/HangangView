package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import dto.NoticeAttachedFileDTO;
import dto.NoticeDTO;

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
public class NoticeDAOImpl implements NoticeDAO {

    @Autowired
    public JdbcTemplate jdbcTemplate;
    
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<NoticeDTO> getAllNotices() {
        String sql = "SELECT * FROM notice";
        return jdbcTemplate.query(sql, new NoticeRowMapper());
    }
    
    @Override
    public List<NoticeDTO> getNoticesPage(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql = "SELECT * FROM notice ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new Object[]{pageSize, offset}, new NoticeRowMapper());
    }
    
    @Override
    public int getNoticeCount() {
        String sql = "SELECT COUNT(*) FROM notice";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public NoticeDTO getNoticeById(Long id) {
        String sql = "SELECT * FROM notice WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new NoticeRowMapper());
    }

    @Override
    public int addNotice(NoticeDTO notice, List<MultipartFile> files) {
        
        String sql = "INSERT INTO notice (title, department, personInCharge, phoneNumber, content, viewCount) VALUES (?, ?, ?, ?, ?, 0)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, notice.getTitle());
            ps.setString(2, notice.getDepartment());
            ps.setString(3, notice.getPersonInCharge());
            ps.setString(4, notice.getPhoneNumber());
            ps.setString(5, notice.getContent());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            long generatedId = key.longValue();
            notice.setId(generatedId);

            
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
    public int updateNotice(NoticeDTO notice, List<MultipartFile> newFiles) {
        String sql = "UPDATE notice SET title = ?, department = ?, personInCharge = ?, phoneNumber = ?, content = ? WHERE id = ?";
        int updateCount = jdbcTemplate.update(sql, notice.getTitle(), notice.getDepartment(), notice.getPersonInCharge(), notice.getPhoneNumber(), notice.getContent(), notice.getId());
        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String filePath = saveFileOnServer(file);
                    addAttachment(notice.getId(), filePath);
                }
            }
        }

        return updateCount;
    }

    @Override
    public int deleteNotice(Long id) {
        String sql = "DELETE FROM notice WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private List<String> getAttachmentsByNoticeId(Long noticeId) {
        String sql = "SELECT file_path FROM notice_attached_files WHERE notice_id = ?";
        return jdbcTemplate.queryForList(sql, new Object[]{noticeId}, String.class);
    }

    public void addAttachment(Long noticeId, String filePath) {
    	
        String fileName = new File(filePath).getName();

        
        String sqlCheck = "SELECT COUNT(*) FROM notice_attached_files WHERE notice_id = ? AND file_path LIKE ?";
        int count = jdbcTemplate.queryForObject(sqlCheck, new Object[]{noticeId, "%" + fileName}, Integer.class);

       
        if (count == 0) {
        String sql = "INSERT INTO notice_attached_files (notice_id, file_path) VALUES (?, ?)";
        jdbcTemplate.update(sql, noticeId, filePath);
    }
   }

    private class NoticeRowMapper implements RowMapper<NoticeDTO> {
        @Override
        public NoticeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            NoticeDTO notice = new NoticeDTO();
            notice.setId(rs.getLong("id"));
            notice.setTitle(rs.getString("title"));
            notice.setDepartment(rs.getString("department"));
            notice.setPersonInCharge(rs.getString("personInCharge"));
            notice.setPhoneNumber(rs.getString("phoneNumber"));
            notice.setContent(rs.getString("content"));
            notice.setRegisterDate(rs.getTimestamp("registerDate"));
            notice.setViewCount(rs.getInt("viewCount")); 
            List<String> attachedFiles = getAttachmentsByNoticeId(rs.getLong("id"));

            return notice;
        }
    }
    

    
    @Override
    public List<NoticeDTO> searchNotices(String searchType, String searchTerm, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql;

        switch (searchType) {
            case "title":
                sql = "SELECT * FROM notice WHERE title LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
                break;
            case "content":
                sql = "SELECT * FROM notice WHERE content LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }

        return jdbcTemplate.query(
            sql,
            new Object[]{"%" + searchTerm + "%", pageSize, offset},
            new NoticeRowMapper()
        );
    }

    @Override
    public int getSearchNoticeCount(String searchType, String searchTerm) {
        String sql;

        switch (searchType) {
            case "title":
                sql = "SELECT COUNT(*) FROM notice WHERE title LIKE ?";
                break;
            case "content":
                sql = "SELECT COUNT(*) FROM notice WHERE content LIKE ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid search type");
        }

        return jdbcTemplate.queryForObject(sql, new Object[]{"%" + searchTerm + "%"}, Integer.class);
    }
    
    @Override
    public void increaseViewCount(Long id) {
        String sql = "UPDATE notice SET viewCount = viewCount + 1 WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteFilesByNoticeId(int noticeId) {
        
        List<String> attachedFiles = getAttachmentsByNoticeId((long) noticeId);

        
        for (String filePath : attachedFiles) {
            deleteFileFromStorage(filePath);
        }

       
        String deleteFilesSql = "DELETE FROM notice_attached_files WHERE notice_id = ?";
        jdbcTemplate.update(deleteFilesSql, noticeId);
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
    public List<NoticeAttachedFileDTO> getAttachedFilesByNoticeId(Long noticeId) {
        
        String sql = "SELECT * FROM notice_attached_files WHERE notice_id = ?";
        return jdbcTemplate.query(sql, new Object[]{noticeId}, new NoticeAttachedFileRowMapper());
    }
    
    public class NoticeAttachedFileRowMapper implements RowMapper<NoticeAttachedFileDTO> {
        @Override
        public NoticeAttachedFileDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
           
            NoticeAttachedFileDTO attachedFile = new NoticeAttachedFileDTO();
            attachedFile.setFileId(rs.getInt("fileId"));
            attachedFile.setNoticeId(rs.getInt("noticeId"));
            attachedFile.setFilePath(rs.getString("filePath"));
            return attachedFile;
        }
    }
    
    @Override
    public void deleteFileById(int fileId) {
        
        String sqlSelectPath = "SELECT file_path FROM notice_attached_files WHERE file_id = ?";
        String filePath = jdbcTemplate.queryForObject(sqlSelectPath, new Object[]{fileId}, String.class);

        
        deleteFileFromStorage(filePath);

        
        String sqlDelete = "DELETE FROM notice_attached_files WHERE file_id = ?";
        jdbcTemplate.update(sqlDelete, fileId);
    }

    
    
	}

