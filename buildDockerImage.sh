#!/usr/bin/env bash

#!/bin/bash

docker build -t anyvr-lemon:$TRAVIS_BUILD_ID .
docker tag anyvr-lemon:$TRAVIS_BUILD_ID $AWS_DOCKER_REGISTRY/anyvr-lemon:$TRAVIS_BUILD_ID
docker push $AWS_DOCKER_REGISTRY/anyvr-lemon:$TRAVIS_BUILD_ID