default: clean build run

clean:
	rm -rf bin/
build:
	mkdir -p bin/
	find . -name "*.java" | xargs javac -g -d bin/ -cp "src/:libs/json-20160810.jar:libs/netty-all-5.0.0.Alpha2-SNAPSHOT.jar:libs/boon-0.4.jar:libs/slf4j-api-1.7.10.jar:libs/slf4j-jdk14-1.7.10.jar:libs/javassist.jar"
	cp -r config/ bin/commands/
run:
	java -cp "./bin:libs/json-20160810.jar:libs/netty-all-5.0.0.Alpha2-SNAPSHOT.jar:libs/boon-0.4.jar:libs/slf4j-api-1.7.10.jar:libs/slf4j-jdk14-1.7.10.jar:libs/javassist.jar" services.Services
