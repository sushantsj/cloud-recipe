# cloud-recipe
These project contains these features.
  1. Creating a Spring boot application(Microservice)
  2. Creating RESTAPIs(CRUD)
  3. Connecting to external Rest Services.
  4. Adding Spring Actuator to get metrics of the application
  5. Use of an external Cloud database(Cloud SQL) for persisting information on GCP.
  6. Creating docker image of the spring boot application.
  7. Push the docker image to gcr.io registry.
  8. Deployment of spring boot application on GKE cluster.
  9. Exposing the service.
  10. Adding spring boot application to prometheus for stat collection.
  11. Creating horizontal pod scaling for the application.
  12. Load balancing/scaling of the spring boot application.

# Creating a Spring boot application(Microservice)
 1. I have created the application on https://start.spring.io/ and selected Project: Maven, Language: Java, Spring Boot Version: 2.5.5, Packaging as Jar, Java 11.
 2. Dependencies: Spring Boot Devtools, Spring Web, Spring Data JPA, MySQL Driver, Spring boot actuator, Prometheus.

# Prequisite
Create your youtube api key to access external apis for youtube and update in youtubeServiceImpl class.
Create your movie api key from themoviedb.org and update in youtubecontroller class.
# Softwares/Technologies used to implement/run/modify/test the application.
1. GKE
2. Cloud SQL to store data.
3. Docker on local system.
4. Jmeter to test load balancing
5. Eclipse/Spring tool Suite/Any other editor.
6. Prometheus installed on GKE.
7. Custom-metric-server installed on GKE.
8. JDK 11 on local machine
9. Maven

# Steps to run the project on local machine.
1. Clone the project.
2. Go the directory where pom.xml is located.
3. Create Cloud SQl on GCP.
4. Change the ip and credentials in application.properties file in src/main/resources. Updated it according to your Cloud SQL credentials.
5. Run "mvn spring-boot:run" to run the application locally.

# Steps to run the application(Microservice) on cloud.
1. Go to the path cloud-recipe/cloud-recipe/recipe/ and run "./mvnw spring-boot:build-image" to create docker image. make sure docker is installed.
2. Once the image is created push the image to your recpository or to Google cloud repository.
3. To push it to GCR follow these steps:
  a) docker tag recipe:2.0 gcr.io/<my-project-id>/recipe
  b) docker push gcr.io/<my-project-id>/recipe
4. Update the image path in the deployment-recipe.yaml
5. Create GKE cluster.
6. Clone the project on the cluster.
7. Deploy Kubernetes-metric-server using kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/download/v0.6.1/components.yaml
8. Deploy prometheus:
  Steps:
  1. Update the config-map.yaml with the metrics path and targets as given below for example:
            scrape_configs:
            - job_name: 'spring-actuator'
              metrics_path: '/actuator/prometheus'
              scrape_interval: 5s
              static_configs:
              - targets: ['34.89.31.32:32405']
  2. Go to the path cloud-recipe/cloud-recipe/recipe/ and kubectl apply -f clusterRole.yaml
  3. kubectl apply -f config-map.yaml
  4. kubectl apply -f prometheus-deployment.yaml. (update the image path based on your repository where image is located).
  5. kubectl apply -f prometheus-service.yaml
  6. kubectl apply -f service-monitor-recipe.yaml #to monitor the our service
  6. kubectl apply -f deployment-recipe.yaml #deplyoment of our application.(update the image path based on your repository where image is located)
  7. Run kubectl get service to see the application running on which port number.
  8. Open the browser http://<machineip>:<port>/swagger-ui.html where application is running.
  9. To see application health, metrics you can check it on: http://<machineip>:<port>/actuator/prometheus, http://<machineip>:<port>/actuator/health
  
# Test Loadbalancing using Horizontal Pod scaling
 1. Deploy the hpa-recipe.yaml file using "kubectl apply -f hpa-recipe.yaml".
 2. You can change this file based on your metric and how you want it to load balance.
 3. Check your hpa using "kubectl get hpa". where target and current metric condition would be shown.
 4. Open jmeter and add the api which you want it to have more load. In this tutorial I am using this api "api/recipe/getall-recipes/{query}".
 5. Add the number of thread and loop count in jmeter and run jmeter.
 6. Now run "kubectl get po -w" to check its repliciation based on cpu utilisation.
# Kubernetes Metric Server.
Metrics Server is a scalable, efficient source of container resource metrics for Kubernetes built-in autoscaling pipelines.
Metrics Server collects resource metrics from Kubelets and exposes them in Kubernetes apiserver through Metrics API for use 
by Horizontal Pod Autoscaler and Vertical Pod Autoscaler. Metrics API can also be accessed by kubectl top, making it easier to debug autoscaling pipelines.
