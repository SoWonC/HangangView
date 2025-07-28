package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dao.WaterRequestAttachedFileDAO;
import dto.WaterRequestAttachedFileDTO;
import dto.WaterRequestDTO;
import service.WaterRequestService;
import spring.AuthInfo;

@Controller
public class WaterRequestController {
    @Autowired
    private WaterRequestService waterRequestService;
    private WaterRequestAttachedFileDAO waterRequestAttachedFileDAO;
    
    @Autowired 
    public WaterRequestController(WaterRequestService waterRequestService, WaterRequestAttachedFileDAO waterRequestAttachedFileDAO) {
        this.waterRequestService = waterRequestService;
        this.waterRequestAttachedFileDAO = waterRequestAttachedFileDAO;
    }
    
    public void setWaterRequestService(WaterRequestService waterRequestService) {
        this.waterRequestService = waterRequestService;
    }

    @GetMapping("/waterRequests")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            Model model) {

        List<WaterRequestDTO> waterRequests;
        int totalWaterRequests;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            
            totalWaterRequests = waterRequestService.getSearchWaterRequestCount(searchType, searchTerm);
            waterRequests = waterRequestService.searchWaterRequests(searchType, searchTerm, page, size);
        } else {
            
            totalWaterRequests = waterRequestService.getWaterRequestCount();
            waterRequests = waterRequestService.getWaterRequestsPage(page, size);
        }

        int totalPages = (int) Math.ceil((double) totalWaterRequests / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("waterRequests", waterRequests);
        model.addAttribute("searchType", searchType); 
        model.addAttribute("searchTerm", searchTerm); 

        return "waterRequestList";
    }

    @GetMapping("/waterRequest")
    public String view(@RequestParam Long id, Model model) {
        WaterRequestDTO waterRequest = waterRequestService.getWaterRequestById(id);
       
        List<WaterRequestAttachedFileDTO> attachedFiles = waterRequestAttachedFileDAO.getFilesByWaterRequestId(id.intValue());
        model.addAttribute("waterRequest", waterRequest);
        model.addAttribute("attachedFiles", attachedFiles);
        return "waterRequestView"; 
    }

    @GetMapping("/waterRequest/add")
    public String addWaterRequestForm(HttpServletRequest request, Model model) {
    	HttpSession session = request.getSession();
    	AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            // 사용자가 로그인하지 않았다면 로그인 페이지로 리디렉트
            return "redirect:/";
        }
        
        WaterRequestDTO waterRequest = new WaterRequestDTO();
        waterRequest.setApplicant(authInfo.getName()); // 로그인한 사용자의 이름을 담당자 필드에 설정
        model.addAttribute("waterRequest", waterRequest);
        return "waterRequestForm"; 
    }
    @PostMapping("/waterRequest/add")
    public String add(@ModelAttribute("waterRequest") WaterRequestDTO waterRequest,
                       @RequestParam("attachedFiles") List<MultipartFile> files, Model model) {
        waterRequestService.addWaterRequest(waterRequest, files);
        return "redirect:/requestSuccess";
    }

    
    @GetMapping("/waterRequest/modify")
    public String modifyWaterRequestForm(@RequestParam Long id, Model model) {
        WaterRequestDTO waterRequest = waterRequestService.getWaterRequestById(id);
        List<WaterRequestAttachedFileDTO> attachedFiles = waterRequestAttachedFileDAO.getFilesByWaterRequestId(id.intValue());
        model.addAttribute("waterRequest", waterRequest);
        model.addAttribute("attachedFiles", attachedFiles);
        return "waterRequestModify"; 
    }

    @PostMapping("/waterRequest/modify")
    @ResponseBody
    public ResponseEntity<?> update(@ModelAttribute("waterRequest") WaterRequestDTO waterRequest,
            @RequestParam("newFiles") List<MultipartFile> newFiles) {
        waterRequestService.updateWaterRequest(waterRequest, newFiles);
        return ResponseEntity.ok().body("Water Request updated successfully");
    }

    @GetMapping("/waterRequest/delete")
    public String delete(@RequestParam Long id) {
        waterRequestService.deleteWaterRequest(id);
        waterRequestService.deleteWaterRequestWithFiles(id);
        return "redirect:/waterRequests";
    }
    
    @GetMapping("/waterRequest/deleteFile")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@RequestParam("fileId") int fileId, @RequestParam("waterRequestId") Long waterRequestId) {
        waterRequestAttachedFileDAO.deleteFileById(fileId);

        return ResponseEntity.ok().build();
    }
    @GetMapping("/requestPage")
    public String requestPage(Model model) {
        
        return "requestPage";
    }
    @GetMapping("/requestSuccess")
    public String requestSuccess(Model model) {
      
        return "requestSuccess";
    }


    
}
