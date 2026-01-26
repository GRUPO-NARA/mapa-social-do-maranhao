FROM python:3.12.12-trixie

WORKDIR /ETL

COPY requirements.txt /ETL

RUN pip install -r requirements.txt

COPY . .

CMD ["python", "tests/teste_docker.py"]