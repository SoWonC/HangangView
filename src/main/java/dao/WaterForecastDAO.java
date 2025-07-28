package dao;

import dto.WaterForecastAttachedFileDTO;
import dto.WaterForecastDTO;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface WaterForecastDAO {
    List<WaterForecastDTO> getAllWaterForecasts();
    WaterForecastDTO getWaterForecastById(Long id);
    int addWaterForecast(WaterForecastDTO waterForecast, List<MultipartFile> files); // 오버로드된 메서드 추가
    int updateWaterForecast(WaterForecastDTO waterForecast, List<MultipartFile> newFiles);
    int deleteWaterForecast(Long id);
    int getWaterForecastCount();
    List<WaterForecastDTO> getWaterForecastsPage(int pageNumber, int pageSize);

   
    List<WaterForecastDTO> searchWaterForecasts(String searchType, String searchTerm, int pageNumber, int pageSize);
    
    
    int getSearchWaterForecastCount(String searchType, String searchTerm);

    
    void increaseViewCount(Long id);
    
    
    void deleteFilesByWaterForecastId(int waterForecastId);
    
    void deleteFileById(int fileId);
    
    List<WaterForecastAttachedFileDTO> getAttachedFilesByWaterForecastId(Long waterForecastId);
}
