package service;

import dao.MemberDao;
import dto.Member;
import exception.MemberNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ChangePasswordService {
  private MemberDao memberDao;
  
  public void changePassword(String email, String oldPassword, String newPassword) {
    Member member = this.memberDao.selectByEmail(email);
    if (member == null)
      throw new MemberNotFoundException(); 
    member.changePassword(oldPassword, newPassword);
  }
  
  public void setMemberDao(MemberDao memberDao) {
    this.memberDao = memberDao;
  }
}
