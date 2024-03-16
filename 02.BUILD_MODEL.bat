@echo off

echo try to create json

curl -X POST "localhost:12237/parse?path=../model/"

pause