package service;

import dao.MemberDao;
import dto.Member;
import exception.DuplicateMemberException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import spring.RegisterRequest;

public class MemberRegisterService {
  @Autowired
  private MemberDao memberDao;
  
  public MemberRegisterService() {}
  
  public MemberRegisterService(MemberDao memberDao) {
    this.memberDao = memberDao;
  }
  
  public Long regist(RegisterRequest req) {
    Member member = this.memberDao.selectByEmail(req.getEmail());
    if (member != null)
      throw new DuplicateMemberException("dup email " + req.getEmail()); 
    Member newMember = new Member(
        req.getEmail(), 
        req.getPassword(), 
        req.getName(), 
        LocalDateTime.now());
    this.memberDao.insert(newMember);
    return newMember.getId();
  }
}
