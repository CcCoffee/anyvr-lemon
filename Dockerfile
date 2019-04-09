FROM gradle:jdk11
USER root
RUN apt-get update && apt-get install -y git autoconf automake libtool gcc make g++ && \
    	git clone https://github.com/xiph/opus.git && \
    	cd opus/ && \
    	git checkout v1.3 && \
    	bash autogen.sh && \
    	./configure && \
    	make install
#USER gradle
COPY --chown=gradle:gradle . /app
WORKDIR /app
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64
RUN ls -la /usr/local/lib/
RUN ./gradlew clean build
RUN ls -la /app/libs
RUN ls -la /app
RUN ls -la /app/build/libs/opusjni/shared

#ENV LD_LIBRARY_PATH /app/libs:/usr/local/lib/
#ENTRYPOINT ["java","-jar","build/libs/anyvr-lemon.jar", "0.0.0.0", "7000"]


FROM openjdk:11
WORKDIR /app
COPY --from=build /app/build/libs/anyvr-lemon.jar /app/anyvr-lemon.jar
COPY --from=build /app/libs/libopusjni.so /app/libs/libopusjni.so
COPY --from=build /usr/local/lib/*.so* /usr/local/lib

ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64
ENV LD_LIBRARY_PATH /app/libs:/usr/local/lib/
##RUN echo "export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64" >> ~/.bashrc
##RUN ls -la /app
##RUN ls -la /app/libs
##RUN chmod 777 /app
##RUN chmod 777 /app/libs
##RUN chmod 777 /app/libs/libopusjni.so
#RUN echo $JAVA_HOME
##RUN ls $JAVA_HOME/include
##RUN ls $JAVA_HOME/include/linux
ENTRYPOINT ["java","-Xmx200M","-Xms20M","-jar","anyvr-lemon.jar", "0.0.0.0", "7000"]