mvn clean install -DskipTests
docker build -t webpool .
docker run -p 8082:8082 webpool
