FROM gcr.io/distroless/java21-debian12:nonroot

WORKDIR /app

ENV TZ="Europe/Oslo"
ENV JAVA_OPTS='-XX:MaxRAMPercentage=90'

COPY spane-mediators/build/libs/*.jar .

CMD ["app.jar"]
