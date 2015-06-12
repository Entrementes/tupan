FROM java:8

RUN apt-key adv --keyserver keyserver.ubuntu.com --recv 7F0CEB10
RUN echo "deb http://repo.mongodb.org/apt/debian wheezy/mongodb-org/3.0 main" | tee /etc/apt/sources.list.d/mongodb-org-3.0.list
RUN apt-get update
RUN apt-get update && apt-get install -y lsb-release haveged mongodb-org
RUN mkdir -p /data/db

ENV server.contextPath /v1
ENV server.port 9999
ENV api.version 1.0
ENV api.id tupan-server
ENV spring.data.mongodb.host localhost
ENV spring.data.mongodb.port 27017
ENV spring.data.mongodb.database tupan
ENV tupan.streamPort 9998
ENV tupan.historyBufferSize 4
ENV tupan.fareDifferentialBase 0.9
ENV tupan.poolingInterval 1000
ENV tupan.fareVariance 0.1

VOLUME /tmp
ADD https://raw.githubusercontent.com/Entrementes/tupan/mvn-repo/org/entrementes/tupan-server/1.0.0/tupan-server-1.0.0.jar app.jar
RUN bash -c 'touch /app.jar'

ADD start.sh /start.sh
RUN chmod 755 /start.sh

EXPOSE 9999
EXPOSE 9010
EXPOSE 9998
EXPOSE 27017

ENTRYPOINT ["/start.sh"]
#docker run -d -p 9010:9010 -p 9999:9999 -p 9998:9998/udp gunisalvo/tupan-server