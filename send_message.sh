#!/bin/bash

message=$1

curl -X POST \
    -H "Content-Type: application/json" \
    -d "{\"message\": \"$message\"}" \
    http://localhost:8080/send
