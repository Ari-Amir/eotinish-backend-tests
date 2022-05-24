FROM gradle:6.1.1-jdk8 as builder
COPY --chown=gradle . /tests
WORKDIR /tests
RUN gradle -s shadowJar
COPY ./src/main/resources/* /tests/build/libs/
ENTRYPOINT exec java -jar /tests/build/libs/eotinish-backend-tests-all.jar

