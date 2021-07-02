package com.ahmad.satkerpolri.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.ahmad.satkerpolri.mapper.AkunMapper;
import com.ahmad.satkerpolri.mapper.SatkerMapper;
import com.ahmad.satkerpolri.model.AkunModel;
import com.ahmad.satkerpolri.model.PnbpPolriModel;
import com.ahmad.satkerpolri.model.SatkerModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcDao {

	public static Logger logger = LogManager.getLogger(JdbcDao.class.getName());

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Environment env;
	
	public List<SatkerModel> getsatker() {			
		//String query = "select kode_satker,nama_satker from kode_satker_polri";
		String query = env.getProperty("data.pnbp.query_satker");
		List<SatkerModel> data =  jdbcTemplate.query(query,new SatkerMapper());
		return data;
	}
	
	public List<AkunModel> getakun() {			
		//String query = "select kode_akun,detail from kode_akun_polri";
		String query = env.getProperty("data.pnbp.query_akun");
		List<AkunModel> data =  jdbcTemplate.query(query,new AkunMapper());
		return data;
	}
	
	public int[] LoadData(List<PnbpPolriModel> data) {

		//String query = "INSERT into pnbp_polri(kode_bank,tahun,bulan,tglbayar_bpn,kode_billing,"
		//		+ "ntpn,kl,satker,akun,description,nominal_akun) "
		//		+ "values(?,?,?,?,?,?,?,?,?,?,?)";
		
		String query = env.getProperty("data.pnbp.query_insert");

		return  this.jdbcTemplate.batchUpdate(query,
				new BatchPreparedStatementSetter() {
								
					@Override
					public void setValues(PreparedStatement ps, int i) {
						try {
							int j=0;
							ps.setObject(j=j+1, data.get(i).getKodebank());
							ps.setObject(j=j+1, data.get(i).getTahun());
							ps.setObject(j=j+1, data.get(i).getBulan());
							ps.setObject(j=j+1, data.get(i).getTglbayarbpn());
							ps.setObject(j=j+1, data.get(i).getTglbuku());
							ps.setObject(j=j+1, data.get(i).getKodebilling());
							ps.setObject(j=j+1, data.get(i).getNtpn());
							ps.setObject(j=j+1, data.get(i).getKl());
							ps.setObject(j=j+1, data.get(i).getSatker());
							ps.setObject(j=j+1, data.get(i).getAkun());
							ps.setObject(j=j+1, data.get(i).getDescription());
							ps.setObject(j=j+1, data.get(i).getNominalakun());

						}catch(SQLException sqle) {
							logger.error("Error LoadData "+sqle);
						}
					}
					
					@Override
					public int getBatchSize() {
						return data.size();
					}
				});
	}
	

}
