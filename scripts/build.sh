#!/bin/bash

set -e

SERVICES=("sh-api-gateway" "sh-auth" "sh-eureka" "sh-user")

export JAVA_HOME="C:/Program Files/Java/jdk-17"
export PATH=$JAVA_HOME/bin:$PATH

for SERVICE in "${SERVICES[@]}"; do
  echo "Building ${SERVICE}..."
  
  cd ../"${SERVICE}"
  mvn clean package -DskipTests
  
  echo "${SERVICE} built successfully."
done
cd ../

echo "All services built successfully."


echo "Starting services with Docker Compose..."
docker-compose -f docker-compose-with-build.yml up -d

