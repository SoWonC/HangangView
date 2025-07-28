package controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.MemberDao;
import dto.Member;
import spring.RegisterRequest;

@Controller
public class RegisterController {
  @Autowired
  DataSource dataSource;
  
  @Autowired
  private MemberDao memberDao;
  
  @GetMapping("/regist")
  public String regist() {
    return "regist";
  }
  
  @PostMapping("/regist")
  public String regists(HttpServletRequest request, Model model) {
    String email = request.getParameter("email");
    String name = request.getParameter("name");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");
    if (!password.equals(confirmPassword)) {
      model.addAttribute("error", "password check");
      return "regist";
    } 
    LocalDateTime currentDateTime = LocalDateTime.now();
    RegisterRequest regReq = new RegisterRequest();
    Member member = new Member(email, name, password, currentDateTime);
    memberDao.insert(member);
    return "registerComplete";
  }
  
  @PostMapping("/checkEmail")
  @ResponseBody
  public String checkId(@RequestParam("email") String email) {
    List<String> emails = this.memberDao.selectEmail();
    if (emails.contains(email))
      return "0"; 
    return "1";
  }
}
