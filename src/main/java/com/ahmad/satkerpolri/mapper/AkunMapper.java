package com.ahmad.satkerpolri.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ahmad.satkerpolri.model.AkunModel;

import org.springframework.jdbc.core.RowMapper;

public class AkunMapper implements RowMapper<AkunModel>{

	@Override
	public AkunModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		AkunModel detail = new AkunModel();
		
		detail.setKodeakun(rs.getString("kode_akun"));
		detail.setDetail(rs.getString("detail"));
		
		return detail;
	}

}