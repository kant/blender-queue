# Project bqueue

## Purpose of this project

/!\ This project is at a Proof of concept stage. It works but it's very rudimentary.

Aims at providing services to build and efficient blender 3D render farm.
Aimas at being a cheap option that can be used to leverage old hardware to contribute to faster renders on personal render farms.

### Features currently include 

* Dividing frame into tiles and distribute rendering on different instances (bare metal, virtual, cloud..)
* Works on windows and linux
* Merging tiles together as if it was rendered by a single machine
* Seing the progress of tiles being ready in a webinterface.
* Render components are completely distributed, there is no notion of coordinator/worker nodes, the only central piece is a messaging broker to broadcast,synchronize data betweeen instances.

### Roadmap features 

* Distribute frames of an animation to render

### 
 
## Setup

Infra relies on a messaging system called Apache Artemis to load balance workloads according to their speed.

# Running with Docker images

Start an artemis broker

```
docker run -it --rm \
  -p 8161:8161 \
  -p 61616:61616 \
  -e ARTEMIS_USERNAME=admin \
  -e ARTEMIS_PASSWORD=admin \
  -e DISABLE_SECURITY=true \
  vromero/activemq-artemis
```


Start blender rest api

```
blender -b -P rest-api.py -- target/renders target/blendfiles
```

```
docker run --rm -e QUARKUS_ARTEMIS_URL=tcp://172.17.0.1:61616 -e BLENDERQUEUE_HOSTNAME=holt bqueue

```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
mvn quarkus:dev
```

Accessing the app : http://localhost:8080

Accessing SwaggerUi : http://localhost:8080/swagger-ui/

Accessing openapi spec of camel rests : http://localhost:8080/camel-openapi

Health UI : http://localhost:8080/health-ui/

Accessing metrics : http://localhost:8080/metrics

Metrics in json with filters on app metrics : `curl -H"Accept: application/json" localhost:8080/metrics/application`

## Packaging and running the application

The application can be packaged using `mvn package`.
It produces the `testing-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/testing-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `mvn package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `mvn package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/testing-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.

## Run local container with specific network and IP address

Optionally you can create a separate local docker network for this app

```
docker network create --driver=bridge --subnet=172.18.0.0/16 --gateway=172.18.0.1 primenet 
```

```
docker stop bqueue
docker rm bqueue
docker rmi bqueue

docker build -f src/main/docker/Dockerfile.fast-jar -t bqueue-fast .
docker build -f src/main/docker/Dockerfile.jvm -t bqueue .
docker build -f src/main/docker/Dockerfile.native -t bqueue-native .

docker run -d --net primenet --ip 172.18.0.10 --name bqueue bqueue
```


Stop or launch multple instances

```
NB_CONTAINERS=2
for (( i=0; i<$NB_CONTAINERS; i++ ))
do
   docker stop bqueue-$i
   docker rm bqueue-$i
done


docker rmi bqueue
docker build -t bqueue .
```

Choose one of methods
```
docker build -f src/main/docker/Dockerfile.fast-jar -t bqueue-fast .
docker build -f src/main/docker/Dockerfile.jvm -t bqueue .
docker build -f src/main/docker/Dockerfile.native -t bqueue-native .```
```
```
for (( i=0; i<$NB_CONTAINERS; i++ ))
do
    docker run -d --net primenet --ip 172.18.0.1$i --name bqueue-$i bqueue
done

```


## Push on dockerhub

```
docker login
docker build -t bqueue -f src/main/docker/Dockerfile.jvm .
docker tag bqueue:latest alainpham/bqueue:latest
```