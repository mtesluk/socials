## RECRUITMENT APP
Application exposes simple REST inteface of social network posts.
It's based on very simple one table:
```
create table post (
date timestamp(6) with time zone not null,
id bigint not null,
view_count bigint not null,
content varchar(1000) not null,
author varchar(255),
primary key (id)
)
```
It could be devided into separated table for User or even for View Counter.

Schema of API and descriptions are available at http://localhost:8080/swagger-ui/index.html

Build docker image:
```docker build . -t mtesluk/socials```

Run docker container:
```docker run -p 8080:8080 -it mtesluk/socials```