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

import dao.WaterViewAttachedFileDAO;
import dto.WaterViewAttachedFileDTO;
import dto.WaterViewDTO;
import service.WaterViewService;
import spring.AuthInfo;

@Controller
public class WaterViewController {

    @Autowired
    private WaterViewService waterViewService;
    private WaterViewAttachedFileDAO waterViewAttachedFileDAO;
    
    @Autowired 
    public WaterViewController(WaterViewService waterViewService, WaterViewAttachedFileDAO waterViewAttachedFileDAO) {
        this.waterViewService = waterViewService;
        this.waterViewAttachedFileDAO = waterViewAttachedFileDAO;
    }
    
    public void setWaterViewService(WaterViewService waterViewService) {
        this.waterViewService = waterViewService;
    }

    @GetMapping("/waterViews")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            Model model) {

        List<WaterViewDTO> waterViews;
        int totalWaterViews;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            totalWaterViews = waterViewService.getSearchWaterViewCount(searchType, searchTerm);
            waterViews = waterViewService.searchWaterViews(searchType, searchTerm, page, size);
        } else {
            totalWaterViews = waterViewService.getWaterViewCount();
            waterViews = waterViewService.getWaterViewsPage(page, size);
        }

        int totalPages = (int) Math.ceil((double) totalWaterViews / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("waterViews", waterViews);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchTerm", searchTerm);

        return "waterViewList";
    }

    @GetMapping("/waterView")
    public String view(@RequestParam Long id, Model model) {
        WaterViewDTO waterView = waterViewService.getWaterViewById(id);
        waterViewService.increaseViewCount(id); 
        List<WaterViewAttachedFileDTO> attachedFiles = waterViewAttachedFileDAO.getFilesByWaterViewId(id.intValue());
        model.addAttribute("waterView", waterView);
        model.addAttribute("attachedFiles", attachedFiles);
        return "waterViewView"; 
    }

    @GetMapping("/waterView/add")
    public String addWaterViewForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        WaterViewDTO waterView = new WaterViewDTO();
        waterView.setPersonInCharge(authInfo.getName());
        model.addAttribute("waterView", waterView);
        return "waterViewForm"; 
    }

    @PostMapping("/waterView/add")
    public String add(HttpServletRequest request, @ModelAttribute("waterView") WaterViewDTO waterView, 
            @RequestParam("attachedFiles") List<MultipartFile> files, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        waterViewService.addWaterView(waterView, files);
        return "redirect:/waterViews";
    }

    @GetMapping("/waterView/modify")
    public String modifyWaterViewForm(@RequestParam Long id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        WaterViewDTO waterView = waterViewService.getWaterViewById(id);
        List<WaterViewAttachedFileDTO> attachedFiles = waterViewAttachedFileDAO.getFilesByWaterViewId(id.intValue());
        model.addAttribute("waterView", waterView);
        model.addAttribute("attachedFiles", attachedFiles);
        return "waterViewModify"; 
    }

    @PostMapping("/waterView/modify")
    @ResponseBody
    public ResponseEntity<?> update(HttpServletRequest request, @ModelAttribute("waterView") WaterViewDTO waterView,
            @RequestParam("newFiles") List<MultipartFile> newFiles) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        waterViewService.updateWaterView(waterView, newFiles);
        return ResponseEntity.ok().body("Water View updated successfully");
    }

    @GetMapping("/waterView/delete")
    public String delete(@RequestParam Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/"; // 비로그인 사용자 또는 관리자가 아닌 경우 리디렉트
        }

        waterViewService.deleteWaterView(id);
        waterViewService.deleteWaterViewWithFiles(id);
        return "redirect:/waterViews";
    }

    @GetMapping("/waterView/deleteFile")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@RequestParam("fileId") int fileId, @RequestParam("waterViewId") Long waterViewId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        waterViewAttachedFileDAO.deleteFileById(fileId);
        return ResponseEntity.ok().build();
    }
}
