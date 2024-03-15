@echo off

echo create jar

call gradlew.bat

echo try build

call gradlew build

echo build complete

pasue