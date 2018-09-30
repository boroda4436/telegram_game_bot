#Telegram game bot

How to start?
- Build project  
 >mvn clean install
- Run with debug options 
> java -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar target\telegram_api_connector-1.0.jar