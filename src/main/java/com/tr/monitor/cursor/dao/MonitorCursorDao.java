package com.tr.monitor.cursor.dao;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class MonitorCursorDao {
	
	 private static final Logger log = LoggerFactory.getLogger(MonitorCursorDao.class);
	 
	 private JdbcTemplate jdbcTemplate = null;
	 
	 @Autowired
	 public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		 this.jdbcTemplate = jdbcTemplate;
	 }
	 
	 public void queryAndLogCursor(int num) {
	     String SQL = "select a.sid as sid, a.user_name as user_name, a.sql_id as sql_id, substr(s.SQL_FULLTEXT,0,100) as sql, a.num_curs as num_curs"
				 + " from v$sql s" 
				 + " join (select o.user_name, o.sql_id, s.sid, count(*) num_curs "
				         + " from v$open_cursor o, v$session s " 
				         + " where  o.sid=s.sid"     
				         + " and   O.cursor_type in ('OPEN','OPEN-RECURSIVE') "
				         + " group by o.user_name,o.sql_id,s.sid "
				         + " order by num_curs desc) a "
				 + " on  s.sql_id = a.sql_id " 
				 + " where a.num_curs > "+num ; 
	     
	    log.info("start querying");
		List rows = this.jdbcTemplate.queryForList(SQL);
		log.info("list size={}",rows.size());
		for(int i=0; i<rows.size();i++) {
			Map map = (Map) rows.get(i);
			log.info("sid={},user_name={},sql_id={},sql={},num_curs={}"
					,map.get("sid")
					,map.get("user_name")
					,map.get("sql_id")
					,map.get("sql")
					,map.get("num_curs"));
			
		}
		
		log.info("==================================");
		
		String SQL2 = "select substr(s.SQL_FULLTEXT, 0, 100) txt, sql_repeat, num_curs,b.sid"
				  + " from v$sql s,"
			      + " (select o1.sql_id, a.num_curs, a.sid, count(*) sql_repeat"
			      + "   from v$open_cursor o1,"
			      + " "         
			      + "         (select num_curs, sid"
			      + "            from (select count(*) num_curs, o.sid"
			      + "                    from v$open_cursor o, v$session s"
			      + "                   where o.sid = s.sid"
			      + "                     and o.user_name = 'CEF_CNR'"
			      + "                     and machine like 'c822rfk%'"
			      + "                     and O.cursor_type in ('OPEN', 'OPEN-RECURSIVE')"
			      + "                   group by o.sid )"			                        			                       
			      + "           where num_curs > 2000) a"
			      + "   where a.sid = o1.sid"
			      + "   group by a.sid,num_curs,sql_id) b"
			 +" where b.sql_id = s.sql_id"
			 +"  and sql_repeat > 10"
			 +" order by sql_repeat desc";
		
		log.info("start query SQL2");
		
		List rows2 = this.jdbcTemplate.queryForList(SQL2);
		log.info("list2 size={}", rows2.size());
		for(int i=0;i<rows2.size();i++){
			Map map = (Map)rows2.get(i);
			log.info("sid={},txt={},sql_repeat={},num_curs={}"
					,map.get("sid")
					,map.get("txt")
					,map.get("sql_repeat")
					,map.get("num_curs"));
		}
				
	 }

}
