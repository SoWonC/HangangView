package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import command.LoginCommand;
import dao.MemberDao;
import spring.AuthInfo;
import spring.AuthService;

@Controller
public class LoginController {
	
	private AuthService authService;
	
	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}
	
	@Autowired
	DataSource dataSource;

	@Autowired
	private MemberDao memberDao;

	@GetMapping({ "/login" })
	public String login(LoginCommand loginCommand, HttpServletRequest request) {
		HttpSession session = request.getSession();
		 AuthInfo authInfo = (AuthInfo) session.getAttribute("authInfo");
		 if (authInfo != null) {
			 return "../../hacheon";
		 }
		return "login";
	}
	
	@PostMapping("/login")
	public String loginCheck(HttpServletRequest request, Model model, HttpSession session) {
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    try {
	        String memberName = memberDao.loginCheck(email, password);

	        if ("error".equals(memberName)) {
	            return "loginFailure";
	        }

	        AuthInfo authInfo = new AuthInfo(email, memberName);
	        session.setAttribute("authInfo", authInfo);

	        return "loginSuccess";
	    } catch (EmptyResultDataAccessException ex) {
	        return "loginFailure";
	    }
	}
		
		
		
	@GetMapping({ "/findId" })
	public String findId() {
		return "findId";
	}

	@PostMapping({ "/findId" })
	@ResponseBody
	public String findEmails(@RequestParam("name") String name) {
		String email = this.memberDao.selectEmailByName(name);
		return email;
	}

	@GetMapping({ "/findPw" })
	public String findPw() {
		return "findPw";
	}

	@PostMapping({ "/findPw" })
	@ResponseBody
	public String findPws(@RequestParam("name") String name, @RequestParam("email") String email) {
		String password = this.memberDao.selectPasswordByNameAndEmail(name, email);
		return password;
	}
}
