package controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dao.MemberDao;
import dto.Member;

@Controller
public class AdminController {
	
	@Autowired
	private MemberDao memberDao;

	@GetMapping("information_management")
	public String information_management() {
		return "infManage";
	}
	
	@GetMapping("water_management")
	public String water_management() {
		return "waterManage";
	}
	
	@GetMapping("member_management")
	public String member_management(Model model) {
		List<Member> list = memberDao.selectAll();
		
		model.addAttribute("list", list);
		return "memberManage";
	}
}
