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

import dao.DroughtForecastAttachedFileDAO;
import dto.DroughtForecastAttachedFileDTO;
import dto.DroughtForecastDTO;
import service.DroughtForecastService;
import spring.AuthInfo;

@Controller
public class DroughtForecastController {

    @Autowired
    private DroughtForecastService droughtForecastService;
    private DroughtForecastAttachedFileDAO droughtForecastAttachedFileDAO;
    
    @Autowired 
    public DroughtForecastController(DroughtForecastService droughtForecastService, DroughtForecastAttachedFileDAO droughtForecastAttachedFileDAO) {
        this.droughtForecastService = droughtForecastService;
        this.droughtForecastAttachedFileDAO = droughtForecastAttachedFileDAO;
    }
    
    public void setDroughtForecastService(DroughtForecastService droughtForecastService) {
        this.droughtForecastService = droughtForecastService;
    }

    @GetMapping("/droughtForecasts")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            Model model) {

        List<DroughtForecastDTO> droughtForecasts;
        int totalDroughtForecasts;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            
            totalDroughtForecasts = droughtForecastService.getSearchDroughtForecastCount(searchType, searchTerm);
            droughtForecasts = droughtForecastService.searchDroughtForecasts(searchType, searchTerm, page, size);
        } else {
           
            totalDroughtForecasts = droughtForecastService.getDroughtForecastCount();
            droughtForecasts = droughtForecastService.getDroughtForecastsPage(page, size);
        }

        int totalPages = (int) Math.ceil((double) totalDroughtForecasts / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("droughtForecasts", droughtForecasts);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchTerm", searchTerm);

        return "droughtForecastList";
    }

    @GetMapping("/droughtForecast")
    public String view(@RequestParam Long id, Model model) {
        DroughtForecastDTO droughtForecast = droughtForecastService.getDroughtForecastById(id);
        droughtForecastService.increaseViewCount(id); 
        List<DroughtForecastAttachedFileDTO> attachedFiles = droughtForecastAttachedFileDAO.getFilesByDroughtForecastId(id.intValue());
        model.addAttribute("droughtForecast", droughtForecast);
        model.addAttribute("attachedFiles", attachedFiles);
        return "droughtForecastView"; 
    }

    @GetMapping("/droughtForecast/add")
    public String addDroughtForecastForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null)  {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        DroughtForecastDTO droughtForecast = new DroughtForecastDTO();
        droughtForecast.setPersonInCharge(authInfo.getName());
        model.addAttribute("droughtForecast", droughtForecast);
        return "droughtForecastForm";
    }

    @PostMapping("/droughtForecast/add")
    public String add(HttpServletRequest request, @ModelAttribute("droughtForecast") DroughtForecastDTO droughtForecast, 
            @RequestParam("attachedFiles") List<MultipartFile> files, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        droughtForecastService.addDroughtForecast(droughtForecast, files);
        return "redirect:/droughtForecasts";
    }

    @GetMapping("/droughtForecast/modify")
    public String modifyDroughtForecastForm(@RequestParam Long id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        DroughtForecastDTO droughtForecast = droughtForecastService.getDroughtForecastById(id);
        List<DroughtForecastAttachedFileDTO> attachedFiles = droughtForecastAttachedFileDAO.getFilesByDroughtForecastId(id.intValue());
        model.addAttribute("droughtForecast", droughtForecast);
        model.addAttribute("attachedFiles", attachedFiles);
        return "droughtForecastModify";
    }

    @PostMapping("/droughtForecast/modify")
    @ResponseBody
    public ResponseEntity<?> update(HttpServletRequest request, @ModelAttribute("droughtForecast") DroughtForecastDTO droughtForecast,
            @RequestParam("newFiles") List<MultipartFile> newFiles) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        droughtForecastService.updateDroughtForecast(droughtForecast, newFiles);
        return ResponseEntity.ok().body("Drought Forecast updated successfully");
    }

    @GetMapping("/droughtForecast/delete")
    public String delete(@RequestParam Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        droughtForecastService.deleteDroughtForecast(id);
        droughtForecastService.deleteDroughtForecastWithFiles(id);
        return "redirect:/droughtForecasts";
    }

    @GetMapping("/droughtForecast/deleteFile")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@RequestParam("fileId") int fileId,
    		@RequestParam("droughtForecastId") Long droughtForecastId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        droughtForecastAttachedFileDAO.deleteFileById(fileId);
        return ResponseEntity.ok().build();
    }
}
