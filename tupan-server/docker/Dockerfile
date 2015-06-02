FROM java:8
VOLUME /tmp
ADD tupan-server.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9999
EXPOSE 9998
ENTRYPOINT ["java","-jar","/app.jar"]