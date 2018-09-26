package com.tr.monitor.cursor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tr.monitor.cursor.dao.MonitorCursorDao;

@Service
public class MonitorCursorService {
	private @Autowired MonitorCursorDao monitorCursorDao;
	private static final Logger log = LoggerFactory.getLogger(MonitorCursorService.class);
	public void start(final int num,int interval) {
		log.info("Monitor start running");
		ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor(); 
		threadPool.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				monitorCursorDao.queryAndLogCursor(num);
			}
		}, 1000, interval, TimeUnit.MILLISECONDS);
	}
}
