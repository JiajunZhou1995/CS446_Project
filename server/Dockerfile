FROM ubuntu:latest
RUN sudo yum install python34
RUN curl -O https://bootstrap.pypa.io/get-pip.py
RUN python3 get-pip.py --user
COPY . /app
WORKDIR /app
RUN pip3 install -r requirements.txt
ENTRYPOINT ["python3"]
CMD ["app.py"]

