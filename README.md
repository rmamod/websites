# Webistes
Android application fetching and displaying a list of websites.

## Technical Choice & Quality
- Picasso is used for image fetching
- RxJava is used for asynchronous computation
- Realm is used as the main database system (I wanted to try the library)
- OkHttp is used for HTTP requests
- Used retrolambda (IDE plugin) for convenience (needs jdk8)


## TODO
- Display a waiting placeholder while fetching list
- use ButterKnife for view binding
- Write Unit test on relevant place
- Improve FastScroller (mine has been inspired by a StackOverflow post and is widely improvable)


## Note
- Photos on AWS are really small so they are stretched to be a minimum visible