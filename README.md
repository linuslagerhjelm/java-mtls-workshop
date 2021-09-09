# Java mTLS Workshop
This repository contains the material for a workshop on how to configure mTLS between two services written in spring.

## Prerequisites
- Java _(16.0.1)_
- Gradle _(7.1.1 (or use the included gradle wrapper))_
- OpenSSL _(LibreSSL 2.8.3)_

A recommendation is to make use of [SDKMAN](https://sdkman.io) to manage Java-related libraries.

## Get started:

1. Clone this repo
2. `cd business-application && gradlew bootRun`
3. `cd api-gw && gradlew bootRun`
4. Access the business-application on [http://localhost:8080/api/hello](http://localhost:8080/api/hello)
5. Access the api-gw on [http://localhost:8081/api/hello-client](http://localhost:8081/api/hello-client)