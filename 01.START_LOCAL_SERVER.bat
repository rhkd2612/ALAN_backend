@echo off

echo Step 1: create jar

call gradlew.bat

echo Step 2: try build

call gradlew build

echo Step 3: ready to start

cd build/libs

java -jar test-0.0.1-SNAPSHOT.jar

pause