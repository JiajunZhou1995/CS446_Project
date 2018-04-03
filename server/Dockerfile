FROM ubuntu:latest
RUN apt-get update -y
RUN apt-get install -y python-pip3 python-dev build-essential
COPY . /app
WORKDIR /app
RUN pip3 install -r requirements.txt
ENTRYPOINT ["python"]
CMD ["app.py"]

