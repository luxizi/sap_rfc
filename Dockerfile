FROM registry.cn-shenzhen.aliyuncs.com/weimeizi/java-1.8.0:latest
COPY target/sap-rfc-1.0-SNAPSHOT.jar /root
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/root/sap-rfc-1.0-SNAPSHOT.jar"]
