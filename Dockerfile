FROM ubuntu:latest

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install -y locales && \
    apt-get install -y tini && \
    apt-get install -y xvfb && \
    apt-get install -y openjdk-17-jdk && \
    apt-get install -y maven 

RUN locale-gen en_US.UTF-8  
ENV LANG en_US.UTF-8  
ENV LANGUAGE en_US:en  
ENV LC_ALL en_US.UTF-8

ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-arm64

ENTRYPOINT ["/usr/bin/tini", "--"]

WORKDIR /work

CMD ["xvfb-run", "mvn", "clean", "install"]