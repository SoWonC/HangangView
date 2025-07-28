package hacheon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


@Repository
public class HacheonListDao {

	private JdbcTemplate jdbcTemplate;
	  
	  public HacheonListDao(DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	  }
	  
	  private RowMapper<HacheonList> haRowMapper = 
			  new RowMapper<HacheonList>() {
		  @Override 
		  public HacheonList mapRow(ResultSet rs, int rowNum) throws SQLException {
			  HacheonList hacheonList = new HacheonList(rs.getString("hacheon_name"),
					  									rs.getString("hacheon_code"),
					  									rs.getString("hacheon_grade"));
					  					
					  					
			 
			  return hacheonList;
		  }
	  };
	  
	  public List<HacheonList> selectAll() {
	    List<HacheonList> results = jdbcTemplate.query("SELECT hacheon_name, hacheon_code,hacheon_grade FROM hacheon_table ORDER BY id ASC;\r\n"
	    		+ "", haRowMapper);
	    return results;
	  }
	  
}
