FROM python:3.12.12-trixie

WORKDIR /ETL

COPY requirements.txt /ETL

RUN pip install -r requirements.txt

COPY . .

ENV PYTHONPATH=/ETL

RUN pytest tests/ -v

CMD ["python", "main.py"]