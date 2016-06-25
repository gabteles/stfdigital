#!/usr/bin/env bash

build_parallely() {
    ./start-npm-proxy.sh
    gradle docker -PnpmRegistry="http://docker:8081/content/groups/npm/"
}

(time build_parallely) 2>&1 | tee build-parallely.log