package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import dto.WaterRequestDTO;
import dto.WaterRequestAttachedFileDTO;
import dao.WaterRequestDAOImpl;
import dao.WaterRequestDAO;

@Service
public class WaterRequestService {

    @Autowired
    private WaterRequestDAOImpl waterRequestDAOImpl;
   
    @Autowired
    private WaterRequestDAO waterRequestDAO;

    public void setWaterRequestDAO(WaterRequestDAOImpl waterRequestDAO) {
        this.waterRequestDAOImpl = waterRequestDAO;
    }

    public int addWaterRequest(WaterRequestDTO waterRequest, List<MultipartFile> files) {
        return waterRequestDAOImpl.addWaterRequest(waterRequest, files);
    }

    public List<WaterRequestDTO> getAllWaterRequests() {
        return waterRequestDAOImpl.getAllWaterRequests();
    }

    public WaterRequestDTO getWaterRequestById(Long id) {
        return waterRequestDAOImpl.getWaterRequestById(id);
    }

    public int updateWaterRequest(WaterRequestDTO waterRequest, List<MultipartFile> newFiles) {
       int updateCount = waterRequestDAOImpl.updateWaterRequest(waterRequest, newFiles);

        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String filePath = waterRequestDAOImpl.saveFileOnServer(file);
                    waterRequestDAOImpl.addAttachment(waterRequest.getId(), filePath);
                }
            }
        }

        return updateCount;
    }

    public int deleteWaterRequest(Long id) {
        return waterRequestDAOImpl.deleteWaterRequest(id);
    }
    
    public int getWaterRequestCount() {
        return waterRequestDAO.getWaterRequestCount();
    }

    public List<WaterRequestDTO> getWaterRequestsPage(int page, int size) {
        return waterRequestDAO.getWaterRequestsPage(page, size);
    }
    
    public List<WaterRequestDTO> searchWaterRequests(String searchType, String searchTerm, int pageNumber, int pageSize) {
        return waterRequestDAO.searchWaterRequests(searchType, searchTerm, pageNumber, pageSize);
    }

    public int getSearchWaterRequestCount(String searchType, String searchTerm) {
        return waterRequestDAO.getSearchWaterRequestCount(searchType, searchTerm);
    }
    
   
    
    public void deleteWaterRequestWithFiles(Long id) {
        List<WaterRequestAttachedFileDTO> attachedFiles = waterRequestDAO.getAttachedFilesByWaterRequestId(id);
        for (WaterRequestAttachedFileDTO attachedFile : attachedFiles) {
            deleteFileFromStorage(attachedFile.getFilePath());
        }

        waterRequestDAO.deleteWaterRequest(id);
    }

    private void deleteFileFromStorage(String filePath) {
       // Implementation for deleting the file from storage
    }
}
