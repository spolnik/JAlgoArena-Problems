FROM openjdk:8

WORKDIR /app
ADD build/libs/* /app/
ADD ProblemsStore /app/ProblemsStore

ENV EUREKA_URL=http://eureka:5000/eureka
EXPOSE 5002

CMD java -Dserver.port=5002 -jar /app/jalgoarena-problems-*.jar