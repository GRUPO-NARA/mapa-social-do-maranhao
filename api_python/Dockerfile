FROM python:3.12-slim

ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1 \
    PIP_NO_CACHE_DIR=1

WORKDIR /app

COPY requirements.txt ./requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

COPY . ./api_python

EXPOSE 8000

CMD ["uvicorn", "api_python.main:apiPython", "--host", "0.0.0.0", "--port", "8000"]
