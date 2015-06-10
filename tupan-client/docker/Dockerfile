FROM java:8

VOLUME /tmp
ADD https://github.com/Entrementes/tupan/blob/mvn-repo/org/entrementes/tupan-client/1.0.0/tupan-client-1.0.0.jar app.jar
RUN bash -c 'touch /app.jar'

ADD start.sh /start.sh
RUN chmod 755 /start.sh

EXPOSE 7777
EXPOSE 9998

ENTRYPOINT ["/start.sh"]
#docker run -d -p 7777:7777 -p 9998:9998/udp -v /root/client.properties:/client.properties gunisalvo/tupan-client