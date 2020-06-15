#!/bin/sh
mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file  \
    -Dfile=/project2/github/steely-glint/pipe-java-client/target/pipe-java-client-1.1.8-SNAPSHOT.jar \
    -DgroupId=pe.pi -DartifactId=pipe-java-client \
    -Dversion=1.1.8-SNAPSHOT -Dpackaging=jar \
    -DlocalRepositoryPath=repo
