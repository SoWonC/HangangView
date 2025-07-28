package dao;

import dto.WaterViewAttachedFileDTO;
import dto.WaterViewDTO;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface WaterViewDAO {
    List<WaterViewDTO> getAllWaterViews();
    WaterViewDTO getWaterViewById(Long id);
    int addWaterView(WaterViewDTO waterView, List<MultipartFile> files); // 오버로드된 메서드 추가
    int updateWaterView(WaterViewDTO waterView, List<MultipartFile> newFiles);
    int deleteWaterView(Long id);
    int getWaterViewCount();
    List<WaterViewDTO> getWaterViewsPage(int pageNumber, int pageSize);

    
    List<WaterViewDTO> searchWaterViews(String searchType, String searchTerm, int pageNumber, int pageSize);
    
    
    int getSearchWaterViewCount(String searchType, String searchTerm);

    
    void increaseViewCount(Long id);
    
    
    void deleteFilesByWaterViewId(int waterViewId);
    
    void deleteFileById(int fileId);
    
    List<WaterViewAttachedFileDTO> getAttachedFilesByWaterViewId(Long waterViewId);
}
