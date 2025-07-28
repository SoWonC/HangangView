package controller;

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

import dao.NoticeAttachedFileDAO;
import dto.NoticeAttachedFileDTO;
import dto.NoticeDTO;
import service.NoticeService;
import spring.AuthInfo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
    private NoticeAttachedFileDAO noticeAttachedFileDAO;
    
    @Autowired 
    public NoticeController(NoticeService noticeService, NoticeAttachedFileDAO noticeAttachedFileDAO) {
        this.noticeService = noticeService;
        this.noticeAttachedFileDAO = noticeAttachedFileDAO;
    }
    
    public void setNoticeService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }



    @GetMapping("/notices")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            Model model) {

        List<NoticeDTO> notices;
        int totalNotices;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            
            totalNotices = noticeService.getSearchNoticeCount(searchType, searchTerm);
            notices = noticeService.searchNotices(searchType, searchTerm, page, size);
        } else {
            
            totalNotices = noticeService.getNoticeCount();
            notices = noticeService.getNoticesPage(page, size);
        }

        int totalPages = (int) Math.ceil((double) totalNotices / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("notices", notices);
        model.addAttribute("searchType", searchType); 
        model.addAttribute("searchTerm", searchTerm); 

        return "noticeList";
    }

    @GetMapping("/notice")
    public String view(@RequestParam Long id, Model model) {
        NoticeDTO notice = noticeService.getNoticeById(id);
        noticeService.increaseViewCount(id); 
        List<NoticeAttachedFileDTO> attachedFiles = noticeAttachedFileDAO.getFilesByNoticeId(id.intValue());
        model.addAttribute("notice", notice);
        model.addAttribute("attachedFiles", attachedFiles);
        return "noticeView"; 
    }

    @GetMapping("/notice/add")
    public String addNoticeForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            // 사용자가 로그인하지 않았다면 로그인 페이지로 리디렉트
            return "redirect:/";
        }

        NoticeDTO notice = new NoticeDTO();
        notice.setPersonInCharge(authInfo.getName()); // 로그인한 사용자의 이름을 담당자 필드에 설정
        model.addAttribute("notice", notice);

        return "noticeForm";
    }
    
    @PostMapping("/notice/add")
    public String add(HttpServletRequest request, @ModelAttribute("notice") NoticeDTO notice, 
            @RequestParam("attachedFiles") List<MultipartFile> files, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            // 사용자가 로그인하지 않았다면 로그인 페이지로 리디렉트
            return "redirect:/";
        }

        noticeService.addNotice(notice, files);
        return "redirect:/notices";
    }
    
    @GetMapping("/notice/modify")
    public String modifyNoticeForm(@RequestParam Long id, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/";
        }

        NoticeDTO notice = noticeService.getNoticeById(id);
        List<NoticeAttachedFileDTO> attachedFiles = noticeAttachedFileDAO.getFilesByNoticeId(id.intValue());
        model.addAttribute("notice", notice);
        model.addAttribute("attachedFiles", attachedFiles);
        return "noticeModify"; 
    }


    @PostMapping("/notice/modify")
    @ResponseBody
    public ResponseEntity<?> update(@ModelAttribute("notice") NoticeDTO notice, 
            @RequestParam("newFiles") List<MultipartFile> newFiles, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        noticeService.updateNotice(notice, newFiles);
        return ResponseEntity.ok().body("Notice updated successfully");
    }


    @GetMapping("/notice/delete")
    public String delete(@RequestParam Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return "redirect:/";
        }

        noticeService.deleteNotice(id);
        noticeService.deleteNoticeWithFiles(id);
        return "redirect:/notices";
    }

    
    @GetMapping("/notice/deleteFile")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@RequestParam("fileId") int fileId, 
            @RequestParam("noticeId") Long noticeId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
        if (authInfo == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }

        noticeAttachedFileDAO.deleteFileById(fileId);
        return ResponseEntity.ok().build();
    }
    
}
