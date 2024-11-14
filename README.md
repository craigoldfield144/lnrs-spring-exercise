## Description
A company search application using Spring Boot 3.3.5
## Requirements
- Maven 3.9.x+
- Java 17
## Testing
Run all tests

```shell
 mvn test
```
## Running
Start the application 

```shell
 mvn spring-boot:run
```

Call the API endpoint

```shell
curl --request POST \
  --url http://localhost:8080/companysearch \
  --header 'content-type: application/json' \
  --header 'x-api-key: your_api_key' \
  --data '{
    "companyName" : "BBC LIMITED",
    "companyNumber" : "06500244"
}'
```

## Developer
Craig Oldfield