package dao;

import dto.NoticeAttachedFileDTO;
import dto.NoticeDTO;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface NoticeDAO {
    List<NoticeDTO> getAllNotices();
    NoticeDTO getNoticeById(Long id);
    int addNotice(NoticeDTO notice, List<MultipartFile> files); // 오버로드된 메서드 추가
    int updateNotice(NoticeDTO notice, List<MultipartFile> newFiles);
    int deleteNotice(Long id);
    int getNoticeCount();
    List<NoticeDTO> getNoticesPage(int pageNumber, int pageSize);

    
   
    List<NoticeDTO> searchNotices(String searchType, String searchTerm, int pageNumber, int pageSize);
    
    
    int getSearchNoticeCount(String searchType, String searchTerm);

 
    void increaseViewCount(Long id);
    
    
    void deleteFilesByNoticeId(int noticeId);
    
    void deleteFileById(int fileId);
    
    List<NoticeAttachedFileDTO> getAttachedFilesByNoticeId(Long noticeId);
}
