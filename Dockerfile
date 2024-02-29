FROM openjdk:17-alpine
LABEL authors="yiyaaa"

COPY ./build/libs/webtoon-zoa-0.0.1-SNAPSHOT.jar /build/libs/webtoon-zoa-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/build/libs/webtoon-zoa-0.0.1-SNAPSHOT.jar"]
