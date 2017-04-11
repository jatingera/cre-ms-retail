FROM tenxtech/java:8
VOLUME /tmp
EXPOSE 9222
ADD cre-ms-retail.jar app.jar
COPY run.sh /usr/bin/run.sh

ENV SPRING_PROFILES_ACTIVE="dev,docker"
ENV JAVA_OPTS="-Xmx512m"

RUN bash -c 'touch /app.jar' && chmod a+x /usr/bin/run.sh

ENTRYPOINT ["/usr/bin/run.sh"]
