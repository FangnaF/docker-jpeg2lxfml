# docker-jpeg2lxfml
Lego JPEG to LXFML convertor for Lego Digital Designer

# Information

Copied Jpeg2Lxfml https://gist.github.com/spudtrooper/4200828 and threw in Docker

# Usage

Create container
docker build -t centos/jpeg2lxfml .

Run container
docker run -d -e "IMAGE_URL=<your_jpeg_url>" centos/jpeg2lxfml

View output
docker logs <container_number>
