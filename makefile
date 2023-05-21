# mvn clean install -DskipTests
docker build -t webpool .
#docker run -p 8082:8082 webpool
docker login --username=mitziuro --password="123qweQWE"
docker image tag webpool mitziuro/ecris:webpool
docker push mitziuro/ecris:webpool
