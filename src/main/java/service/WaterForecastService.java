package service;

import dto.WaterForecastAttachedFileDTO;
import dto.WaterForecastDTO;
import dao.WaterForecastDAO;
import dao.WaterForecastDAOImpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WaterForecastService {

	@Autowired
    private WaterForecastDAOImpl waterForecastDAOImpl;
	
	@Autowired
	private WaterForecastDAO waterForecastDAO;

    public void setWaterForecastDAO(WaterForecastDAOImpl waterForecastDAO) {
        this.waterForecastDAOImpl = waterForecastDAO;
    }

    public int addWaterForecast(WaterForecastDTO waterForecast, List<MultipartFile> files) {
        return waterForecastDAOImpl.addWaterForecast(waterForecast, files);
    }

    public List<WaterForecastDTO> getAllWaterForecasts() {
        return waterForecastDAOImpl.getAllWaterForecasts();
    }

    public WaterForecastDTO getWaterForecastById(Long id) {
        return waterForecastDAOImpl.getWaterForecastById(id);
    }

    public int updateWaterForecast(WaterForecastDTO waterForecast, List<MultipartFile> newFiles) {
    	int updateCount = waterForecastDAOImpl.updateWaterForecast(waterForecast, newFiles);

        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String filePath = waterForecastDAOImpl.saveFileOnServer(file);
                    waterForecastDAOImpl.addAttachment(waterForecast.getId(), filePath);
                }
            }
        }

        return updateCount;
    }

    public int deleteWaterForecast(Long id) {
        return waterForecastDAOImpl.deleteWaterForecast(id);
    }
    
    public int getWaterForecastCount() {
        return waterForecastDAO.getWaterForecastCount();
    }

    public List<WaterForecastDTO> getWaterForecastsPage(int page, int size) {
        return waterForecastDAO.getWaterForecastsPage(page, size);
    }
    
    public List<WaterForecastDTO> searchWaterForecasts(String searchType, String searchTerm, int pageNumber, int pageSize) {
        return waterForecastDAO.searchWaterForecasts(searchType, searchTerm, pageNumber, pageSize);
    }

    public int getSearchWaterForecastCount(String searchType, String searchTerm) {
        return waterForecastDAO.getSearchWaterForecastCount(searchType, searchTerm);
    }
    
    public void increaseViewCount(Long id) {
    	waterForecastDAOImpl.increaseViewCount(id);
    }
    
    public void deleteWaterForecastWithFiles(Long id) {
        List<WaterForecastAttachedFileDTO> attachedFiles = waterForecastDAO.getAttachedFilesByWaterForecastId(id);
        for (WaterForecastAttachedFileDTO attachedFile : attachedFiles) {
            deleteFileFromStorage(attachedFile.getFilePath());
        }
        waterForecastDAO.deleteWaterForecast(id);
    }

    private void deleteFileFromStorage(String filePath) {
        
    }
}
