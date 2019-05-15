FROM gradle:jdk11 as build
USER root
RUN apt-get update && apt-get install -y git autoconf automake libtool gcc make g++ && \
    	git clone https://github.com/xiph/opus.git && \
    	cd opus/ && \
    	git checkout v1.3 && \
    	bash autogen.sh && \
    	./configure && \
    	make install
USER gradle
COPY --chown=gradle:gradle . /app
WORKDIR /app
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64
ENV LD_LIBRARY_PATH /app/libs:/usr/local/lib
RUN mkdir -p tests
RUN gradle clean build --info


FROM openjdk:11
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64
ENV LD_LIBRARY_PATH /app/libs:/usr/local/lib
WORKDIR /app
COPY --from=build /app/build/libs/anyvr-lemon.jar /app/anyvr-lemon.jar
COPY --from=build /app/libs/libopusjni.so /app/libs/libopusjni.so
COPY --from=build /usr/local/lib/* /usr/local/lib/
ENTRYPOINT ["java","-Xmx200M","-Xms20M","-jar","anyvr-lemon.jar", "0.0.0.0", "7000"]
