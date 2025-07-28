package hacheon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class HacheonDetailListDao {

	private JdbcTemplate jdbcTemplate;

	public HacheonDetailListDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private RowMapper<HacheonDetailList> haRowMapper = 
			new RowMapper<HacheonDetailList>() {
		@Override
		public HacheonDetailList mapRow(ResultSet rs, int rowNum) throws SQLException {
			HacheonDetailList hacheonDetailList = new HacheonDetailList();
			hacheonDetailList.setHacheonName(rs.getString("hacheon_name"));
			hacheonDetailList.setHacheonCode(rs.getString("hacheon_code"));
			hacheonDetailList.setHacheonGrade(rs.getString("hacheon_grade"));
			hacheonDetailList.setStartLocationProvince(rs.getString("start_location_province"));
			hacheonDetailList.setStartLocationDistrict(rs.getString("start_location_district"));
			hacheonDetailList.setStartLocationTown(rs.getString("start_location_town"));
			hacheonDetailList.setStartLocationBoundary(rs.getString("start_location_boundary"));
			hacheonDetailList.setStartPlanFrequency(rs.getString("start_plan_frequency"));
			hacheonDetailList.setStartPlanFloodVolume(rs.getString("start_plan_flood_volume"));
			hacheonDetailList.setStartPlanFloodLevel(rs.getString("start_plan_flood_level"));
			hacheonDetailList.setStartPlanWidth(rs.getString("start_plan_width"));
			hacheonDetailList.setEndLocationProvince(rs.getString("end_location_province"));
			hacheonDetailList.setEndLocationDistrict(rs.getString("end_location_district"));
			hacheonDetailList.setEndLocationTown(rs.getString("end_location_town"));
			hacheonDetailList.setEndLocationBoundary(rs.getString("end_location_boundary"));
			hacheonDetailList.setEndPlanFrequency(rs.getString("end_plan_frequency"));
			hacheonDetailList.setEndPlanFloodVolume(rs.getString("end_plan_flood_volume"));
			hacheonDetailList.setEndPlanFloodLevel(rs.getString("end_plan_flood_level"));
			hacheonDetailList.setEndPlanWidth(rs.getString("end_plan_width"));
			hacheonDetailList.setRiverLength(rs.getString("river_length"));
			hacheonDetailList.setRiverPlanDeclaredDate(rs.getString("river_plan_declared_date"));
			hacheonDetailList.setRiverPlanExtension(rs.getString("river_plan_extension"));
			hacheonDetailList.setRiverPlanUnestablished(rs.getString("river_plan_unestablished"));
			hacheonDetailList.setEuroLength(rs.getString("euro_length"));
			hacheonDetailList.setDrainageArea(rs.getString("drainage_area"));
			hacheonDetailList.setRiverRemedyTotal(rs.getString("river_remedy_total"));
			hacheonDetailList.setRiverRemedyCompletionZone(rs.getString("river_remedy_completion_zone"));
			hacheonDetailList.setRiverRemedyReinforcementZone(rs.getString("river_remedy_reinforcement_zone"));
			return hacheonDetailList;
			}
		};


		public HacheonDetailList selectAll(String hacheon_code) {
			List<HacheonDetailList> results = jdbcTemplate.query("SELECT * FROM hacheon_table where hacheon_code=? ORDER BY id ASC", haRowMapper,hacheon_code);
			return results.isEmpty() ? null : results.get(0);
		}


	}
