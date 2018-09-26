# MonitorCursor
A tool for monitoring open cursors for oracle, it can be used in detecting cursor leak. The tool will check the v$ views on a scheduled interval(defalut is 10 mintues) and print the most cursor consuming sqls in the log. 

Usage:
1. Database configerations are in config.properties and config-prod.properties, for beta and for prod respectively (The user should have DBA privilege)
2. Package with : mvn package. It will produce and fat jar which contains all the dependencies
3. Run with ：java -jar MonitorCursor-1.0.0.jar prod 500 600000  “prod” means run in production environment, "500" means only print sqls which open more than 500 cursors , "600000" means the running interval
4. The result is in log: /local/logs/esb/esb-app-monitorcursor.log