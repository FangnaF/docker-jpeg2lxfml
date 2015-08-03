# docker-jpeg2lxfml
Lego JPEG to LXFML convertor for Lego Digital Designer

# Information

## What does this do?..

### Create custom image from CentOS 6.6 image

* Copied Jpeg2Lxfml https://gist.github.com/spudtrooper/4200828 and threw in Docker
* Installed Wget Java, Ruby, Rubygems, and Gist gem
* Pull image off internets
* Install java class and run
* Upload to Gist via Gist gem
* Spit out URL link

# Usage

### Create container

`docker build -t centos/jpeg2lxfml .`

### Run container

`docker run -d -e "IMAGE_URL=<your_jpeg_url>" centos/jpeg2lxfml`

### View output

`docker logs <container_number>`
