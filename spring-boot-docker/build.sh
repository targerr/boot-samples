#!/bin/bash
version=`awk '/<version>[^<]+<\/version>/{gsub(/<version>|<\/version>/,"",$1);print $1;exit;}' pom.xml`
echo $version
mvn clean package -DskipTests
docker build -t boot:$version .
docker tag boot:$version boot:$version
#docker push boot:$version
#git tag -a $version -m "v$version"
#git push origin $version