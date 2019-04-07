FROM gradle:jdk11

COPY --chown=gradle:gradle . /app
WORKDIR /app
USER root
RUN apt-get update && \
    	apt-get install -y git autoconf automake libtool gcc make openjdk-11-jdk && \
    	git clone https://github.com/xiph/opus.git && \
    	cd opus/ && \
    	git checkout v1.3 && \
    	bash autogen.sh && \
    	./configure && \
    	make install
RUN gradle clean build
#FROM openjdk:11
#WORKDIR /app
#COPY --from=build /app/build/libs/anyvr-lemon.jar /app/anyvr-lemon.jar
#ENTRYPOINT ["java","-Xmx200M","-Xms20M","-jar","anyvr-lemon.jar", "0.0.0.0", "7000"]