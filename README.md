# Coding Challenge: Single File API for Task Management

I was asked to fulfill all requirements for this coding challenge on a single Java file

<details>
  <summary>
<h2>Challenge description</h2>
  </summary>
You're working on a system that helps to manage tasks on a todo list.
Your job is to implement an endpoint that allows these tasks to be updated.
In order to achieve this, you should use the Hibernate and Spring frameworks.</p>
### Application
- Spring Boot application version 2.0.5
- `spring-boot-starter-data-jpa` and `spring-boot-starter-web` modules included.
- Database access is fully configured.
- The application is compiled using JDK 8.
- Hibernate version 5.2.17.Final

### Database
Tasks are stored in a relational database. The DB schema looks like this:
```
CREATE TABLE task (
  id bigint NOT NULL,
  description varchar(200) NOT NULL,
  priority bigint,
  PRIMARY KEY (id)
);
```
### Endpoint
Sending a request:
`PUT /tasks/{id}`
with request body:
```
{
   "description": "task's description"
   "priority": 5
}
```
should update the description and priority of the task with id = ID. Changes should be stored in the database.
### Your tasks
- Configure the Task class as a Hibernate entity
- Implement an endpoint as described above. Furthermore, it should meet the following requirements:
- Endpoint should return error code 404 if a task with the given id does not exist.
In response, the server should return the following JSON:
```
    {
       "message": "Cannot find task with given id"
       "status": 404
    }
```
- Endpoint should return error code 400 if the task description is null (or key "description" is not present in the request JSON).
In response, the server should return the following JSON:
```
    {
       "message": "Task description is required"
       "status": 400
    }
```
- Endpoint should return error code 200 when the task is successfully updated. The response body should be the same as the request body, for example:
```
{
   "description": "task's description"
   "priority": 5
}
```
### Remark
You can place more than one class declaration in the editor. Please remember not to use public classes.
At first, when you run the tests on the initial code, the Spring application will fail at startup. It will start successfully only after you do complete some of the requirements.
</details>
