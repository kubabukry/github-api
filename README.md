# Github Repository Browser
> This project is using GitHub REST API for browsing repositories based on the owner's username
> 
>[Github REST API documentation](https://developer.github.com/v3)


## General Information
- This application allows to retrieve specific information about given user's repositories
- With a provided API it can exclude those repositories which are forks, listing the following information:
  - Repository names
  - Owner login
  - Branch names
  - For each branch, it’s name and last commit sha
- In case of not existing GitHub username, API consumer should receive modified responses for 404 and 406 status codes
- The goal of creating it is to learn features of reactive programming using Spring WebFlux in Spring Boot 3


## Technologies Used
- Java 17
- Spring Boot 3
- Spring WebFlux
- Gradle


## Reference documentation
Rest API references from Github Docs for used endpoints:
-  [List repositories for a user](https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user)
- [List branches](https://docs.github.com/en/rest/branches/branches?apiVersion=2022-11-28#list-branches)




## Setup
For properly set environmental variables and gradle installed, follow the setup below:
- Clone this repository
`$ git clone https://github.com/kubabukry/github-api.git`
- Go into the project directory
`$ cd <project-directory>`
- Run the following command to build application
`$ ./gradlew build`
- Run the following command to start the application
`$ ./gradlew bootRun`


## Usage
- Main endpoint for getting combined data in response

`GET http://localhost:<server-port>/{github-username}/combined`

- Helper endpoint for getting all repositories of given owner

`GET http://localhost:<server-port>/{github-username}`

- Helper endpoint for getting all branches of given owner and repository

`GET http://localhost:<server-port>/{github-username}/{repository-name}/branches`

Server port is `8080` by default


## Author
Created by Jakub Bukry
