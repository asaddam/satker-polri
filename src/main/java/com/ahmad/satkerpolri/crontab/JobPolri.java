package com.ahmad.satkerpolri.crontab;

import com.ahmad.satkerpolri.restcontroller.LoadPNBP;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobPolri {

public static Logger logger = LogManager.getLogger(JobPolri.class.getName());
	
	@Autowired
	private Environment env;
	
	@Autowired
	private LoadPNBP loadPNBP;
	
	@Scheduled(cron = "${data.polri.crontab.time}")
    public void scheduleHost() {
		if(env.getProperty("data.polri.crontab.run").equals("yes")) {
			try{  	
				String kirim = loadPNBP.Load();
				logger.info("Job Load Polri :"+kirim);	
			}catch(Exception e){
				logger.error("Error Load Polri "+e);
				e.printStackTrace();
			}
		}
	}
}
