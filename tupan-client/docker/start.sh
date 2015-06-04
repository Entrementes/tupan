#!/bin/sh
/etc/init.d/mongod start &
java -jar /app.jar