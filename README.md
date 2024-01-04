# **Movie Management System Documentation**
The REST API Application for Movie Management as a Recruitment Task for Java Developer.

Smoke tests were created in Postman as a mean to assert consistency of application's basic functionality. They are to be imported from file found in [documentation directory](/documentation/MovieManagmentSystemTests.postman_collection.json).
# **Requirements**
Before running, you need to enter the API key for omdbapi.com in the application.properties file.

# **Endpoints**
### 1. Search Movie:

```
- Endpoint: GET /api/movies/search

 Description: Search for a movie using its title. Returns detailed information about the movie, including title, description, genre, director, and poster.
Parameters:
    - title (required): The title of the movie to be searched.
```
### 2. Add to Favorites:

```
- Endpoint: POST /api/movies/favorites

Description: Add a movie to the list of favorites. Requires the title of the movie to be added.
Parameters:
    - title (required): The title of the movie to be added to favorites.
```
### 3. Get Favorites:

```
- Endpoint: GET /api/movies/favorites

Description: Retrieve a list of favorite movies.
```