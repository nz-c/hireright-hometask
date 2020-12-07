FROM tomcat:9.0
EXPOSE 8080

RUN mkdir /usr/local/hometask
RUN mkdir /usr/local/hometask/data

ADD dataWeather.json /usr/local/hometask/data/
ADD dataGeoLocation.json /usr/local/hometask/data/

ADD target/HomeTask.war /usr/local/tomcat/webapps/

RUN sed -i 's/<\/Host>/<Context docBase=\"HomeTask.war\" path=\"\/\" reloadable=\"true\"\/><\/Host>/' /usr/local/tomcat/conf/server.xml

RUN chmod 777 /usr/local/tomcat/bin/catalina.sh
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]