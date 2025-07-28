package config;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import dao.MemberDao;
import hacheon.HacheonDetailListDao;
import hacheon.HacheonListDao;
import spring.AuthService;

@Configuration
@EnableTransactionManagement
public class MemberConfig {
  @Bean(destroyMethod = "close")
  public DataSource dataSource() {
    DataSource ds = new DataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUrl("jdbc:postgresql://localhost:5432/postgres");
    ds.setUsername("postgres");
    ds.setPassword("12345678");
    return ds;
  }
  
  @Bean
  public PlatformTransactionManager transactionManager() {
    DataSourceTransactionManager tm = new DataSourceTransactionManager();
    tm.setDataSource(dataSource());
    return tm;
  }
  
  @Bean
  public MemberDao memberDao() {
    return new MemberDao(dataSource());
  }
  
  @Bean
	public AuthService authService() {
		AuthService authService = new AuthService();
		authService.setMemberDao(memberDao());
		return authService;
  }
  
  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
      return new JdbcTemplate(dataSource);
  }
  
  @Bean
  public HacheonListDao hacheonListDao() {
  	return new HacheonListDao(dataSource());
  }
  
  @Bean
  public HacheonDetailListDao hacheonDetailListDao() {
  	return new HacheonDetailListDao(dataSource());
  }
  
}