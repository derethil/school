FROM python:3.10.8

WORKDIR /app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
CMD ["python", "consumer.py", "-rb", "usu-cs5260-push-requests", "-dwt", "widgets"]

