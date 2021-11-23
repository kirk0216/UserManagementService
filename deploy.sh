#!/bin/bash

./mvnw package
docker build . -t cst8277/ums-app:local
docker save cst8277/ums-app:local > ums.tar
microk8s ctr image import ums.tar