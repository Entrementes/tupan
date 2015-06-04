FROM java:8

RUN apt-key adv --keyserver keyserver.ubuntu.com --recv 7F0CEB10
RUN apt-get update && apt-get install -y lsb-release
RUN apt-get install haveged -y
RUN echo "deb http://repo.mongodb.org/apt/debian wheezy/mongodb-org/3.0 main" | tee /etc/apt/sources.list.d/mongodb-org-3.0.list
RUN apt-get update
RUN apt-get install -y mongodb-org mongodb-org-server mongodb-org-mongos
RUN mkdir -p /data/db


VOLUME /tmp
ADD tupan-server.jar app.jar
RUN bash -c 'touch /app.jar'

ADD start.sh /start.sh
RUN chmod 755 /start.sh

EXPOSE 9999
EXPOSE 9998
EXPOSE 27017

ENTRYPOINT ["/start.sh"]