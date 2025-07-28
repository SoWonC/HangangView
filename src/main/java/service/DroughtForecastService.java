package service;

import dto.DroughtForecastAttachedFileDTO;
import dto.DroughtForecastDTO;
import dao.DroughtForecastDAO;
import dao.DroughtForecastDAOImpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DroughtForecastService {

	@Autowired
    private DroughtForecastDAOImpl droughtForecastDAOImpl;
	
	@Autowired
	private DroughtForecastDAO droughtForecastDAO;

    public void setDroughtForecastDAO(DroughtForecastDAOImpl droughtForecastDAO) {
        this.droughtForecastDAOImpl = droughtForecastDAO;
    }

    public int addDroughtForecast(DroughtForecastDTO droughtForecast, List<MultipartFile> files) {
        return droughtForecastDAOImpl.addDroughtForecast(droughtForecast, files);
    }

    public List<DroughtForecastDTO> getAllDroughtForecasts() {
        return droughtForecastDAOImpl.getAllDroughtForecasts();
    }

    public DroughtForecastDTO getDroughtForecastById(Long id) {
        return droughtForecastDAOImpl.getDroughtForecastById(id);
    }

    public int updateDroughtForecast(DroughtForecastDTO droughtForecast, List<MultipartFile> newFiles) {
    	int updateCount = droughtForecastDAOImpl.updateDroughtForecast(droughtForecast, newFiles);

        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String filePath = droughtForecastDAOImpl.saveFileOnServer(file);
                    droughtForecastDAOImpl.addAttachment(droughtForecast.getId(), filePath);
                }
            }
        }

        return updateCount;
    }

    public int deleteDroughtForecast(Long id) {
        return droughtForecastDAOImpl.deleteDroughtForecast(id);
    }
    
    public int getDroughtForecastCount() {
        return droughtForecastDAO.getDroughtForecastCount();
    }

    public List<DroughtForecastDTO> getDroughtForecastsPage(int page, int size) {
        return droughtForecastDAO.getDroughtForecastsPage(page, size);
    }
    
    public List<DroughtForecastDTO> searchDroughtForecasts(String searchType, String searchTerm, int pageNumber, int pageSize) {
        return droughtForecastDAO.searchDroughtForecasts(searchType, searchTerm, pageNumber, pageSize);
    }

    public int getSearchDroughtForecastCount(String searchType, String searchTerm) {
        return droughtForecastDAO.getSearchDroughtForecastCount(searchType, searchTerm);
    }
    
    public void increaseViewCount(Long id) {
    	droughtForecastDAOImpl.increaseViewCount(id);
    }
    
    public void deleteDroughtForecastWithFiles(Long id) {
        List<DroughtForecastAttachedFileDTO> attachedFiles = droughtForecastDAO.getAttachedFilesByDroughtForecastId(id);
        for (DroughtForecastAttachedFileDTO attachedFile : attachedFiles) {
            deleteFileFromStorage(attachedFile.getFilePath());
        }
        droughtForecastDAO.deleteDroughtForecast(id);
    }

    private void deleteFileFromStorage(String filePath) {
        
    }
}
