FROM java:8

VOLUME /tmp
ADD tupan-client.jar app.jar
RUN bash -c 'touch /app.jar'

ADD start.sh /start.sh
RUN chmod 755 /start.sh

EXPOSE 9999
EXPOSE 9998

ENTRYPOINT ["/start.sh"]