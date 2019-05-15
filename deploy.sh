#!/usr/bin/env bash

if [ "$(docker ps -aq -f name=anyvr-lemon)" ]; then
        if [ "$(docker ps -q -f name=anyvr-lemon)" ]; then  # check if docker container currently running
        		echo "Stop Docker"
                docker stop anyvr-lemon
        fi
        echo "rm anyvr-lemon"
        docker rm anyvr-lemon
        docker run -d --name anyvr-lemon -p 7000:7000/udp -v /home/anyvr/logs/anyvr-lemon:/app/logs $1
        echo "Docker run anyvr-lemon"
else
        docker run -d --name anyvr-lemon -p 7000:7000/udp -v /home/anyvr/logs/anyvr-lemon:/app/logs $1
fi



