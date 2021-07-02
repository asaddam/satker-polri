package com.ahmad.satkerpolri.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ahmad.satkerpolri.model.SatkerModel;

import org.springframework.jdbc.core.RowMapper;

public class SatkerMapper implements RowMapper<SatkerModel>{

	@Override
	public SatkerModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		SatkerModel detail = new SatkerModel();
		
		detail.setKodesatker(rs.getString("kode_satker"));
		detail.setNamasatker(rs.getString("nama_satker"));
		
		return detail;
	}

}