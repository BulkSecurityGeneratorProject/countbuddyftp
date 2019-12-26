SPRING_PROFILES_ACTIVE=dev,swagger,no-liquibase
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://admin:admin@ec2-3-15-135-106.us-east-2.compute.amazonaws.com:8761/eureka
SPRING_CLOUD_CONFIG_URI=http://admin:admin@ec2-3-15-135-106.us-east-2.compute.amazonaws.com:8761/config
SPRING_DATASOURCE_URL=jdbc:mysql://mastertekdb.cfyototaodfv.us-east-2.rds.amazonaws.com:3306/countbuddy_new_test?useUnicode=true&characterEncoding=utf8&useSSL=false
SPRING_DATASOURCE_USERNAME=masterTek6634
SPRING_DATASOURCE_PASSWORD=105957Grk.
application_ftpDirectory=/home/ramazan/publicDirectory
application_passowordFile=/home/ramazan/myftpusers.properties
application_ftpPort=21
SERVER_PORT=9096
sudo java -jar ftpcountbuddy-0.0.1-SNAPSHOT.war --server.port=$SERVER_PORT --application.ftpPort=$application_ftpPort --application.ftpDirectory=$application_ftpDirectory --application.passowordFile=$application_passowordFile --spring.profiles.active=$SPRING_PROFILES_ACTIVE --spring.cloud.config.url=$SPRING_CLOUD_CONFIG_URI --eureka.client.service.url.defaultzone=$EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE
