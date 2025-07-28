package service;

import dto.WaterViewAttachedFileDTO;
import dto.WaterViewDTO;
import dao.WaterViewDAO;
import dao.WaterViewDAOImpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WaterViewService {

    @Autowired
    private WaterViewDAOImpl waterViewDAOImpl;

    @Autowired
    private WaterViewDAO waterViewDAO;

    public void setWaterViewDAO(WaterViewDAOImpl waterViewDAO) {
        this.waterViewDAOImpl = waterViewDAO;
    }

    public int addWaterView(WaterViewDTO waterView, List<MultipartFile> files) {
        return waterViewDAOImpl.addWaterView(waterView, files);
    }

    public List<WaterViewDTO> getAllWaterViews() {
        return waterViewDAOImpl.getAllWaterViews();
    }

    public WaterViewDTO getWaterViewById(Long id) {
        return waterViewDAOImpl.getWaterViewById(id);
    }

    public int updateWaterView(WaterViewDTO waterView, List<MultipartFile> newFiles) {
        int updateCount = waterViewDAOImpl.updateWaterView(waterView, newFiles);

        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String filePath = waterViewDAOImpl.saveFileOnServer(file);
                    waterViewDAOImpl.addAttachment(waterView.getId(), filePath);
                }
            }
        }

        return updateCount;
    }

    public int deleteWaterView(Long id) {
        return waterViewDAOImpl.deleteWaterView(id);
    }

    public int getWaterViewCount() {
        return waterViewDAO.getWaterViewCount();
    }

    public List<WaterViewDTO> getWaterViewsPage(int page, int size) {
        return waterViewDAO.getWaterViewsPage(page, size);
    }

    public List<WaterViewDTO> searchWaterViews(String searchType, String searchTerm, int pageNumber, int pageSize) {
        return waterViewDAO.searchWaterViews(searchType, searchTerm, pageNumber, pageSize);
    }

    public int getSearchWaterViewCount(String searchType, String searchTerm) {
        return waterViewDAO.getSearchWaterViewCount(searchType, searchTerm);
    }

    public void increaseViewCount(Long id) {
        waterViewDAOImpl.increaseViewCount(id);
    }

    public void deleteWaterViewWithFiles(Long id) {
        List<WaterViewAttachedFileDTO> attachedFiles = waterViewDAO.getAttachedFilesByWaterViewId(id);
        for (WaterViewAttachedFileDTO attachedFile : attachedFiles) {
            deleteFileFromStorage(attachedFile.getFilePath());
        }
        waterViewDAO.deleteWaterView(id);
    }

    private void deleteFileFromStorage(String filePath) {
       
    }
}
