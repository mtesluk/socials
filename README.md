## RECRUITMENT APP

Task overview:
```Please prepare a Springboot maven project that will expose a CRUD API for the following object (object defined in pseudo-code):

SocialNetworkPost {

   Date postDate

   String author

   String content

   Number viewCount

}

 
Aside from the CRUD API, the application should expose an endpoint for retrieving posts with the top ten highest view counts

The application should integrate with a database (you can use H2 integrated in-memory database) to facilitate operations exposed using the API

Please provide the test coverage and all other project facilities that are important in your opinion

When designing the API please consider scalability and performance aspects
```


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