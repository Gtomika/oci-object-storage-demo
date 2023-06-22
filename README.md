# OCI object storage demo

This is a Spring Boot app that uses the OCI SDK to connect to 
a certain bucket in the Object Storage Service. It can upload 
and download files from this bucket, for which an API is 
provided.

# How to run

 - Create the bucket and set its name and namespace in `application.yaml`.
 - Create API key on some user. Make sure this user has the permissions to 
manage objects in the bucket.
 - Create correct OCI `config` file and set the path to it 
and the profile name in `application.yaml`.