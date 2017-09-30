# Movies
Popular movie app with data provided by _TheMovieDB.com_.

## How To Use Movies
### Initial Start-up

Upon initial start-up of _Movies_, A grid view of movie posters will be shown. This list is the "top rated" movies according to _TheMovieDB_. An overflow menu on the toolbar allows the user to select from three lists: top rated, most popular, and favorites. Each list item can be selected to launch a details screen of that movie. These details include release date, rating, synopsis,  comments of the movie from users of _TheMovieDB.com_, and thumbnail images for trailers on _YouTube_. The thumbnail images, when selected, launch the selected trailer in the _YouTube_ app.

### Favorite A Movie

The favorites list is stored on a local SQLite database. In order to add a movie to the favorites list, select a movie from either the "top rated" or "most popular" list. Once on the details screen of the selected movie is displayed, click the button that displays "MAKE FAVORITE". When a movie is a favorite, the same button will display "REMOVE FAVORITE". Clicking the button will remove the movie from the favorites list.
