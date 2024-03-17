@echo off

echo try to create json

set /p userPath="Enter the path: "

curl -X POST "localhost:12237/parse?path=%userPath%"

pause