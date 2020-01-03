SPRING_PROFILES_ACTIVE=prod,swagger,no-liquibase
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://admin:admin@ec2-3-15-135-106.us-east-2.compute.amazonaws.com:8761/eureka
SPRING_CLOUD_CONFIG_URI=http://admin:admin@ec2-3-15-135-106.us-east-2.compute.amazonaws.com:8761/config
SPRING_DATASOURCE_URL=jdbc:mysql://mastertekdb.cfyototaodfv.us-east-2.rds.amazonaws.com:3306/countbuddy_new_test?useUnicode=true&characterEncoding=utf8&useSSL=false
SPRING_DATASOURCE_USERNAME=masterTek6634
SPRING_DATASOURCE_PASSWORD=105957Grk.
application_ftpDirectory=/home/ubuntu/publicDirectory
application_passowordFile=/home/ubuntu/myftpusers.properties
application_ftpPort=21
SERVER_PORT=8086
sudo java -jar -Xmx16048m /home/ubuntu/Documents/versions/ftpcountbuddy-0.0.1-SNAPSHOT.war --spring.profiles.active=$SPRING_PROFILES_ACTIVE
