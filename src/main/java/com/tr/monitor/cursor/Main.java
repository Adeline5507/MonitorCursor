package com.tr.monitor.cursor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		String contextPath = null;
		int cursorThrottleNum = 200;
		int interval = 600000;
		
		if(args.length>0 && args[0].equals("prod")) {
			log.info("run in prod mode");
			contextPath = "classpath:applicationContext-prod.xml";
		} else {
			log.info("run in beta mode");
			contextPath = "classpath:applicationContext.xml";
		}
		
		if(args.length>1){
			cursorThrottleNum = Integer.parseInt(args[1]);
		}
		
		if(args.length>2){
			interval = Integer.parseInt(args[2]);
		}
		
		log.info("starting monitor, contextPath={},cursorThrottleNum={},interval={}",contextPath,cursorThrottleNum,interval);
		
		ApplicationContext actx = new ClassPathXmlApplicationContext(contextPath);
		MonitorCursorService service = (MonitorCursorService) actx.getBean("monitorCursorService");
		service.start(cursorThrottleNum,interval);
		
	}
	
}
