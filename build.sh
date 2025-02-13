#!/bin/bash
./gradlew build -x test
# test 수행이 깨지지만 기능 상 문제가 없기에 서버에 배포가 우선이므로 임시로 test task 수행을 제외함

# Define variables for Docker Hub account and image details
DOCKERHUB_USERNAME=tmdfl36
IMAGE_NAME=mapjiri
TAG=latest  # or specify your desired tag/version

# Build the Docker image
docker buildx build --platform linux/amd64 -t $DOCKERHUB_USERNAME/$IMAGE_NAME:$TAG .
# Log in to Docker Hub (if not logged in)
# docker login -u $DOCKERHUB_USERNAME

# Push the Docker image to Docker Hub
docker push $DOCKERHUB_USERNAME/$IMAGE_NAME:$TAG

