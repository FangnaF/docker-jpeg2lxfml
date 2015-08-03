#
# docker-jpeg2lxfml
#
# Run with:
#    docker build -t centos/jpeg2lxfml .
#    docker run -d -e "IMAGE_URL=<your_jpeg_url>" centos/jpeg2lxfml
#    docker logs <container_number>
#

# Pull base image.
FROM centos:6.6

# Install.
RUN yum install -y wget java java-devel ruby rubygems

# Define working directory.
WORKDIR /root

# Add files.
ADD . /root

# Define default command.
ENTRYPOINT /bin/bash start.sh
