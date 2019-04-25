#!/usr/bin/env bash

#!/bin/bash

docker build -t anyvr-lemon:$TRAVIS_BUILD_ID .
docker tag anyvr-lemon:$TRAVIS_BUILD_ID $AWS_DOCKER_REGISTRY/anyvr-lemon:$TRAVIS_BUILD_ID
docker push $AWS_DOCKER_REGISTRY/anyvr-lemon:$TRAVIS_BUILD_ID

# Deploy Image
ssh -i ~/.ssh/travis_key anyvr@159.89.19.236 "bash -s" < deploy.sh "$AWS_DOCKER_REGISTRY/anyvr-lemon:$TRAVIS_BUILD_ID"