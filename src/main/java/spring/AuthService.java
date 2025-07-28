package spring;

import dao.MemberDao;
import dto.Member;
import exception.WrongIdPasswordException;

public class AuthService {

	private MemberDao memberDao;
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	public AuthInfo authenticate(String email, String password) {
		Member member = memberDao.selectByEmail(email);
		if (member == null) {
			throw new WrongIdPasswordException();
		}
		if (!member.matchPassword(password)) {
			throw new WrongIdPasswordException();
		}
		return new AuthInfo(member.getEmail(),
				member.getName());
	}
}
