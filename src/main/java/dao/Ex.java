package dao;

import java.time.LocalDateTime;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import config.MemberConfig;
import dto.Member;

@ComponentScan
public class Ex {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx =
				new AnnotationConfigApplicationContext(MemberConfig.class);
		MemberDao memberDao = 
				ctx.getBean("memberDao", MemberDao.class);
//		System.out.println(memberDao.selectPasswordByNameAndEmail("lee", "leeemail"));
		
//		System.out.println(memberDao.selectAll());
		
//		System.out.println(memberDao.selectById(5));
		
		LocalDateTime currentDateTime = LocalDateTime.now();
//		
//		Member member = new Member("sampleemail", "samplename", "123", currentDateTime);
//		memberDao.insert(member);
//		System.out.println(memberDao.selectAll());
		
//		System.out.println(memberDao.selectByEmail("leeemail"));
		
//		Member member = new Member("admin", "admin", "1234", currentDateTime);
//		memberDao.insert(member);
		
//		System.out.println(memberDao.loginCheck("admin", "1234"));
		
//		System.out.println(memberDao.selectNameByEmail("admins"));
		
		System.out.println(memberDao.selectCctvName());
		
		
	}
}
