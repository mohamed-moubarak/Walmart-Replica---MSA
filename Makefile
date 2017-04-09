default: clean build run

clean:
	rm -rf bin/
build:
	mkdir -p bin/
	javac -g -d bin/ -cp "src/:libs/HikariCP-java6-2.3.1.jar:libs/postgresql-9.3-1102.jdbc41.jar:libs/netty-all-5.0.0.Alpha2-SNAPSHOT.jar:libs/boon-0.4.jar:libs/slf4j-api-1.7.10.jar:libs/slf4j-jdk14-1.7.10.jar:libs/javassist.jar" src/Services.java
	cp -r config/ bin/
run:
	java -cp "./bin:libs/HikariCP-java6-2.3.1.jar:libs/postgresql-9.3-1102.jdbc41.jar:libs/netty-all-5.0.0.Alpha2-SNAPSHOT.jar:libs/boon-0.4.jar:libs/slf4j-api-1.7.10.jar:libs/slf4j-jdk14-1.7.10.jar:libs/javassist.jar" Services