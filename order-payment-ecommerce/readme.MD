#Springboot Microservices with Eureka Service Registry, Config Server, Cloud API Gateway 
**************
a) run maven command (mvn clean compile package) a
b) Running on Docker, use following commands:
docker build --pull --rm -f "Dockerfile" -t service-registry:latest "."

docker build --pull --rm -f "Dockerfile" -t configserver:latest "."

docker build --pull --rm -f "Dockerfile" -t apigateway:latest "."

docker build --pull --rm -f "Dockerfile" -t auth-service:latest "."

docker build --pull --rm -f "Dockerfile" -t orderservice:latest "."

docker build --pull --rm -f "Dockerfile" -t productservice:latest "."

docker build --pull --rm -f "Dockerfile" -t paymentservice:latest "."


docker-compose up


#************************************************************************************************
# NOTE: As we have config service, after running Service Registry, run Config Service before running others (for local testing).

# Check Service Regisry (http://localhost:8761)

# Config Server ( by checking from registry, you can find out the location of the application configuration fetched from git).

# If you are running locally (make sure zipkin is running at http://localhost:9411/zipkin/, info@https://zipkin.io/pages/quickstart.html)

# To test the application, add requried data 
1. Create User (http://localhost:7777/authenticate/signup) - ROLE_ADMIN and ROLE_USER can use
2. Login user (http://localhost:7777/authenticate/login) - To get token and other information 
3. To fetch token use http://localhost:7777/authenticate/refreshtoken where 'refreshToken' required to provide (previous refreshToken value).

4. Create product (http://localhost:8081/product) by ROLE_USER and with Bearer token (if expire, use login api to get token)

5. Get  product

6. Place Order 

7. Get Order Details 
Note: The order details API need to have header key for authorization.
Authrizatio: Bearer <token>

8. Reduce product quantiy 
9. Remove product (As ROLE_ADMIN)

10. All APIs address through Gatway Service (http://localhost:9090) as well. 



NOTE: Unit tests are not working (requierd to work/fix).





