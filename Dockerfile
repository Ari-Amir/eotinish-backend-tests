FROM gradle:6.1.1-jdk8 as builder
COPY --chown=gradle . /rscomptests
WORKDIR /rscomptests
RUN gradle -s shadowJar
COPY ./src/main/kotlin/tests/* /rscomptests/build/libs/
ENTRYPOINT exec java -jar /rscomptests/build/libs/eotinish-system-tests-all.jar

