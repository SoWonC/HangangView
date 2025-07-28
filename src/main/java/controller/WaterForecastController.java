package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dao.WaterForecastAttachedFileDAO;
import dto.WaterForecastAttachedFileDTO;
import dto.WaterForecastDTO;
import service.WaterForecastService;
import spring.AuthInfo;

@Controller
public class WaterForecastController {

    @Autowired
    private WaterForecastService waterForecastService;
    private WaterForecastAttachedFileDAO waterForecastAttachedFileDAO;
    
    @Autowired 
    public WaterForecastController(WaterForecastService waterForecastService, WaterForecastAttachedFileDAO waterForecastAttachedFileDAO) {
        this.waterForecastService = waterForecastService;
        this.waterForecastAttachedFileDAO = waterForecastAttachedFileDAO;
    }
    
    public void setWaterForecastService(WaterForecastService waterForecastService) {
        this.waterForecastService = waterForecastService;
    }

    @GetMapping("/waterForecasts")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            Model model) {

        List<WaterForecastDTO> waterForecasts;
        int totalWaterForecasts;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            
            totalWaterForecasts = waterForecastService.getSearchWaterForecastCount(searchType, searchTerm);
            waterForecasts = waterForecastService.searchWaterForecasts(searchType, searchTerm, page, size);
        } else {
            
            totalWaterForecasts = waterForecastService.getWaterForecastCount();
            waterForecasts = waterForecastService.getWaterForecastsPage(page, size);
        }

        int totalPages = (int) Math.ceil((double) totalWaterForecasts / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("waterForecasts", waterForecasts);
        model.addAttribute("searchType", searchType); 
        model.addAttribute("searchTerm", searchTerm); 

        return "waterForecastList";
    }

    @GetMapping("/waterForecast")
    public String view(@RequestParam Long id, Model model) {
        WaterForecastDTO waterForecast = waterForecastService.getWaterForecastById(id);
        waterForecastService.increaseViewCount(id); 
        List<WaterForecastAttachedFileDTO> attachedFiles = waterForecastAttachedFileDAO.getFilesByWaterForecastId(id.intValue());
        model.addAttribute("waterForecast", waterForecast);
        model.addAttribute("attachedFiles", attachedFiles);
        return "waterForecastView"; 
    }

    @GetMapping("/waterForecast/add")
    public String addWaterForecastForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        WaterForecastDTO waterForecast = new WaterForecastDTO();
        waterForecast.setPersonInCharge(authInfo.getName());
        model.addAttribute("waterForecast", waterForecast);
        return "waterForecastForm"; 
    }

    @PostMapping("/waterForecast/add")
    public String add(HttpServletRequest request, @ModelAttribute("waterForecast") WaterForecastDTO waterForecast, 
            @RequestParam("attachedFiles") List<MultipartFile> files, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        waterForecastService.addWaterForecast(waterForecast, files); // Now passing 'files' to the service method as well.
        return "redirect:/waterForecasts";
    }

    @GetMapping("/waterForecast/modify")
    public String modifyWaterForecastForm(@RequestParam Long id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        WaterForecastDTO waterForecast = waterForecastService.getWaterForecastById(id);
        List<WaterForecastAttachedFileDTO> attachedFiles = waterForecastAttachedFileDAO.getFilesByWaterForecastId(id.intValue());
        model.addAttribute("waterForecast", waterForecast);
        model.addAttribute("attachedFiles", attachedFiles);
        return "waterForecastModify"; 
    }

    @PostMapping("/waterForecast/modify")
    @ResponseBody
    public ResponseEntity<?> update(HttpServletRequest request, @ModelAttribute("waterForecast") WaterForecastDTO waterForecast,
            @RequestParam("newFiles") List<MultipartFile> newFiles) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        waterForecastService.updateWaterForecast(waterForecast, newFiles);
        return ResponseEntity.ok().body("Water Forecast updated successfully");
    }

    @GetMapping("/waterForecast/delete")
    public String delete(@RequestParam Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        waterForecastService.deleteWaterForecast(id);
        waterForecastService.deleteWaterForecastWithFiles(id);
        return "redirect:/waterForecasts";
    }

    @GetMapping("/waterForecast/deleteFile")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@RequestParam("fileId") int fileId, @RequestParam("waterForecastId") Long waterForecastId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        waterForecastAttachedFileDAO.deleteFileById(fileId);
        return ResponseEntity.ok().build();
    }
}
