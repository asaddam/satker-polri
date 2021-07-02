package com.ahmad.satkerpolri.restcontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ahmad.satkerpolri.dao.JdbcDao;
import com.ahmad.satkerpolri.model.AkunModel;
import com.ahmad.satkerpolri.model.PnbpPolriModel;
import com.ahmad.satkerpolri.model.SatkerModel;
import com.ahmad.satkerpolri.utils.dateformat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadPNBP {
	
	private static Logger logger = LogManager.getLogger(LoadPNBP.class.getName());
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JdbcDao jdbcDao;
	
	@GetMapping(value = "/polri")
	public String Load() {
		String hasiltext = "";
		StringBuffer hasil = new StringBuffer();
		BufferedReader br = null;
		int maxCountGroup=Integer.parseInt(env.getProperty("data.pnbp.maxcountgroup"));
		int maxDataLength=Integer.parseInt(env.getProperty("data.pnbp.maxdatalength"));
		String delimiter = env.getProperty("data.pnbp.delimiter");
		String sCurrentLine = "";
		
		try {
			
			HashMap<String, String> mapsatker = new HashMap<String, String>();
			List<SatkerModel> listsatker = jdbcDao.getsatker();
			for(SatkerModel satker:listsatker) {
				mapsatker.put(satker.getKodesatker(), satker.getNamasatker());
			}
			
			HashMap<String, String> mapakun = new HashMap<String, String>();
			List<AkunModel> listakun = jdbcDao.getakun();
			for(AkunModel akun:listakun) {
				mapakun.put(akun.getKodeakun(), akun.getDetail());
			}
			
			String folder = env.getProperty("data.pnbp.folder");
			File f = new File(folder);
			File[] fs = f.listFiles();
			logger.info("Folder Size :"+fs.length);
			for(File file:fs) {
				if(file.isFile()) {
					ArrayList<PnbpPolriModel> listpnbp = new ArrayList<PnbpPolriModel>();
					int i=0,j=0,i2=0;
					if(!file.getName().endsWith(".Ack")) {			
						br = new BufferedReader(new FileReader(file.getAbsolutePath()));
						while ((sCurrentLine = br.readLine()) != null){
							String[] data = sCurrentLine.split(delimiter);
							
							if(i>0) {
								if(sCurrentLine.length()>=maxDataLength) {
									
									PnbpPolriModel pnbppolri = new PnbpPolriModel();
									pnbppolri.setKodebank(data[0]);
									pnbppolri.setTahun(data[1]);
									pnbppolri.setBulan(data[2]);
									pnbppolri.setTglbayarbpn(dateformat.formatDate5(data[3]));
									pnbppolri.setTglbuku(data[3].substring(0, 5).replace("-", ""));
									pnbppolri.setKodebilling(data[4]);
									pnbppolri.setNtpn(data[5]);
									pnbppolri.setKl(data[6]);
									pnbppolri.setSatker(data[7]);
									pnbppolri.setAkun(data[8]);
									pnbppolri.setDescription(data[9]);
									pnbppolri.setNominalakun(data[10]);
									
									if(mapsatker.get(pnbppolri.getSatker()) != null) {
										if(mapakun.get(pnbppolri.getAkun()) != null) {
											if(j==0){
												listpnbp = new ArrayList<PnbpPolriModel>();
											}
											listpnbp.add(pnbppolri);
											j++;
	
											if(j==maxCountGroup){
												j=0;
												if(listpnbp.size()>0) {
													i2++;
													int[] insert = jdbcDao.LoadData(listpnbp);
													if(insert != null) {
														logger.info("Tambah data :"+i2);
													}
												}
											}
										}
									}	
									
								}
							}
							i++;
						}
					}
					if(listpnbp.size()>0) {
						int[] insert = jdbcDao.LoadData(listpnbp);
						if(insert != null) {
								logger.info("Tambah data Akhir :"+listpnbp.size()+" dari "+Integer.toString(i));
						}
					}
					br.close();
					File renamefile = new File(file.getAbsoluteFile()+".Ack");
					file.renameTo(renamefile);
					hasil.append(file.getName()+"|");
				}
			}
			
		}catch(Exception e) {
			logger.error("Error Load PNBP "+e);
		}
		
		if(hasil.length()>0) {
			hasiltext = hasil.toString();
			hasil.setLength(0);
		}else {
			hasiltext = "Tidak Ada Data Diproses";
		}
		return hasiltext;
	}
}
