package dao;

import dto.DroughtForecastAttachedFileDTO;
import dto.DroughtForecastDTO;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface DroughtForecastDAO {
    List<DroughtForecastDTO> getAllDroughtForecasts();
    DroughtForecastDTO getDroughtForecastById(Long id);
    int addDroughtForecast(DroughtForecastDTO droughtForecast, List<MultipartFile> files); // 오버로드된 메서드 추가
    int updateDroughtForecast(DroughtForecastDTO droughtForecast, List<MultipartFile> newFiles);
    int deleteDroughtForecast(Long id);
    int getDroughtForecastCount();
    List<DroughtForecastDTO> getDroughtForecastsPage(int pageNumber, int pageSize);

    
    List<DroughtForecastDTO> searchDroughtForecasts(String searchType, String searchTerm, int pageNumber, int pageSize);
    
    
    int getSearchDroughtForecastCount(String searchType, String searchTerm);

    
    void increaseViewCount(Long id);
    
    
    void deleteFilesByDroughtForecastId(int droughtForecastId);
    
    void deleteFileById(int fileId);
    
    List<DroughtForecastAttachedFileDTO> getAttachedFilesByDroughtForecastId(Long droughtForecastId);
}
