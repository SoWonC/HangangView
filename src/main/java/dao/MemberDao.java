package dao;

import dto.Member;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class MemberDao {
  private JdbcTemplate jdbcTemplate;
  
  public MemberDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }
  
  private RowMapper<Member> memRowMapper = 
		  new RowMapper<Member>() {
	  @Override 
	  public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
		  Member member = new Member(rs.getString("email"),
				  					rs.getString("name"),
				  					 rs.getString("password"),
				  					 rs.getTimestamp("registerDateTime").toLocalDateTime());
		  member.setId(rs.getLong("id"));
		  return member;
	  }
  };
  
  public List<Member> selectAll() {
    List<Member> results = jdbcTemplate.query("select * from member", memRowMapper);
    return results;
  }
  
  public Member selectByEmail(String email) {
    List<Member> results = jdbcTemplate.query(
    		"select * from member where email = ?",
    		memRowMapper, email);
    
    return results.isEmpty() ? null : results.get(0);
  }
  
  public Member selectById(Long id) {
	  List<Member> results = jdbcTemplate.query(
	    		"select * from member where id = ?",
	    		memRowMapper, id);
	    
	    return results.isEmpty() ? null : results.get(0);
  }
  
  public List<String> selectEmail() {
    String sql = "select email from member";
    return this.jdbcTemplate.queryForList(sql, String.class);
  }
  
  public List<String> selectRegDate() {
	  String sql = "select registerDateTime from member";
	  return this.jdbcTemplate.queryForList(sql, String.class);
  }
  
  public String loginCheck(String email, String password) {
	    String sql = "select name from member where email = ? and password = ?";
	    try {
	        String memberName = this.jdbcTemplate.queryForObject(sql, String.class, email, password);
	        return memberName != null ? memberName : "error";
	    } catch (EmptyResultDataAccessException ex) {
	        return "error"; 
	    }
	}
  
  public String selectEmailByName(String name) {
    String sql = "select count(email) from member where name = ?";
    String sqls = "select email from member where name = ?";
    String a = this.jdbcTemplate.queryForObject(sql, String.class, name);
    int b = Integer.parseInt(a);
    if (b == 0)
      return "0"; 
    return this.jdbcTemplate.queryForObject(sqls, String.class,  name );
  }
  
  public String selectNameByEmail(String email) {
	  String sql = "select name from member where email = ?";
	  try {
	  String results = jdbcTemplate.queryForObject(sql, String.class, email);
	  return results;
	  } catch (EmptyResultDataAccessException e) {
	  return null;
	  }
  }
  
  public String selectPasswordByNameAndEmail(String name, String email) {
    String sql = "select count(password) from member where name = ? and email = ?";
    String sqls = "select password from member where name = ? and email = ?";
    String a = jdbcTemplate.queryForObject(sql, String.class, name, email);
    int b = Integer.parseInt(a);
    if (b == 0)
      return "0"; 
    return this.jdbcTemplate.queryForObject(sqls, String.class, name, email);
  }
  
  public void insert(Member member) {
    String sql = "insert into member(email, name, password, registerDateTime) values(?,?,?,?)";
    LocalDateTime currentDateTime = LocalDateTime.now();
    this.jdbcTemplate.update(sql, member.getEmail(), member.getName(), member.getPassword(), currentDateTime);
  }
  
  public List<String> selectCctvName() {
	  String sql = "select name from cctv";
	  return this.jdbcTemplate.queryForList(sql, String.class);
  }
  
  public List<String> selectCctvwlobscd() {
	     String sql = "select wlobscd from cctv";
	     return this.jdbcTemplate.queryForList(sql, String.class);
	  }

}
