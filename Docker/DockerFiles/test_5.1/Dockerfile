FROM postgres:13

COPY ./init/init.sql /docker-entrypoint-initdb.d/
COPY ./config/postgresql.conf /etc/postgresql/custom.conf

ENV POSTGRES_USER=chocokon
ENV POSTGRES_PASSWORD=abc123
ENV POSTGRES_DB=testDB

EXPOSE 8081

CMD ["postgres", "-c", "config_file=/etc/postgresql/custom.conf"]
