FROM ubuntu:latest

RUN apt-get update && apt-get install -y software-properties-common
RUN add-apt-repository ppa:openjdk-r/ppa && apt-get update
RUN apt-get install -y git autoconf automake libtool gcc make openjdk-11-jdk g++ && \
    	git clone https://github.com/xiph/opus.git && \
    	cd opus/ && \
    	git checkout v1.3 && \
    	bash autogen.sh && \
    	./configure && \
    	make install
COPY . /app
WORKDIR /app
RUN apt-get install -y git autoconf automake libtool gcc make openjdk-11-jdk
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64
RUN ls -la /usr/local/lib/
RUN ./gradlew clean build
RUN ls -la /app/libs
RUN ls -la /app
RUN ls -la /app/build/libs
ENTRYPOINT ["java","-Djava.library.path=/app/libs","-Xmx200M","-Xms20M","-jar","build/libs/anyvr-lemon.jar", "0.0.0.0", "7000"]


#FROM ubuntu:latest
#WORKDIR /app
#RUN apt-get update && apt-get install -y software-properties-common
#RUN add-apt-repository ppa:openjdk-r/ppa && apt-get update
#RUN apt-get install -y git autoconf automake libtool gcc make openjdk-11-jdk
#COPY --from=build /app/build/libs/anyvr-lemon.jar /app/anyvr-lemon.jar
#COPY --from=build /app/libs/libopusjni.so /app/libs/libopusjni.so
#ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64
##RUN echo "export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64" >> ~/.bashrc
##RUN ls -la /app
##RUN ls -la /app/libs
##RUN chmod 777 /app
##RUN chmod 777 /app/libs
##RUN chmod 777 /app/libs/libopusjni.so
#RUN echo $JAVA_HOME
##RUN ls $JAVA_HOME/include
##RUN ls $JAVA_HOME/include/linux
#ENTRYPOINT ["java","-Djava.library.path=/app/libs","-Xmx200M","-Xms20M","-jar","anyvr-lemon.jar", "0.0.0.0", "7000"]