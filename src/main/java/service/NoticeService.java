package service;

import dto.NoticeAttachedFileDTO;
import dto.NoticeDTO;
import dao.NoticeDAO;
import dao.NoticeDAOImpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoticeService {

	@Autowired
    private NoticeDAOImpl noticeDAOImpl;
	
	@Autowired
	private NoticeDAO noticeDAO;

    public void setNoticeDAO(NoticeDAOImpl noticeDAO) {
        this.noticeDAOImpl = noticeDAO;
    }

    public int addNotice(NoticeDTO notice, List<MultipartFile> files) {
        
        return noticeDAOImpl.addNotice(notice, files);
    }

    public List<NoticeDTO> getAllNotices() {
        return noticeDAOImpl.getAllNotices();
    }

    public NoticeDTO getNoticeById(Long id) {
        return noticeDAOImpl.getNoticeById(id);
    }

    public int updateNotice(NoticeDTO notice, List<MultipartFile> newFiles) {
    	int updateCount = noticeDAOImpl.updateNotice(notice, newFiles);

        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String filePath = noticeDAOImpl.saveFileOnServer(file);
                    noticeDAOImpl.addAttachment(notice.getId(), filePath);
                }
            }
        }

        return updateCount;
    }

    public int deleteNotice(Long id) {
        return noticeDAOImpl.deleteNotice(id);
    }
    
 
    public int getNoticeCount() {
        return noticeDAO.getNoticeCount();
    }

    
    public List<NoticeDTO> getNoticesPage(int page, int size) {
        return noticeDAO.getNoticesPage(page, size);
    }
    
  
    
    public List<NoticeDTO> searchNotices(String searchType, String searchTerm, int pageNumber, int pageSize) {
        
        return noticeDAO.searchNotices(searchType, searchTerm, pageNumber, pageSize);
    }

    
    public int getSearchNoticeCount(String searchType, String searchTerm) {
        
        return noticeDAO.getSearchNoticeCount(searchType, searchTerm);
    }
    
    public void increaseViewCount(Long id) {
    	noticeDAOImpl.increaseViewCount(id);
    }
    
    public void deleteNoticeWithFiles(Long id) {
       
        List<NoticeAttachedFileDTO> attachedFiles = noticeDAO.getAttachedFilesByNoticeId(id);
        for (NoticeAttachedFileDTO attachedFile : attachedFiles) {
            deleteFileFromStorage(attachedFile.getFilePath());
        }

       
        noticeDAO.deleteNotice(id);
    }

    	private void deleteFileFromStorage(String filePath) {
    	   
    	}
    
    
    
}
