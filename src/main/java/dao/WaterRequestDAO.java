package dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import dto.WaterRequestAttachedFileDTO;
import dto.WaterRequestDTO;

public interface WaterRequestDAO {
	
	List<WaterRequestDTO> getAllWaterRequests();
    WaterRequestDTO getWaterRequestById(Long id);
    int addWaterRequest(WaterRequestDTO waterRequest, List<MultipartFile> files); // 오버로드된 메서드 추가
    int updateWaterRequest(WaterRequestDTO waterRequest, List<MultipartFile> newFiles);
    int deleteWaterRequest(Long id);
    int getWaterRequestCount();
    List<WaterRequestDTO> getWaterRequestsPage(int pageNumber, int pageSize);

   
    List<WaterRequestDTO> searchWaterRequests(String searchType, String searchTerm, int pageNumber, int pageSize);
    
    
    int getSearchWaterRequestCount(String searchType, String searchTerm);

 
   
    
    
    void deleteFilesByWaterRequestId(int waterRequestId);
    
    void deleteFileById(int fileId);
    
    List<WaterRequestAttachedFileDTO> getAttachedFilesByWaterRequestId(Long waterRequestId);
}



