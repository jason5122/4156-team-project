# Guide for doing manual end-to-end testing
Before doing any end-to-end tests, follow these steps
1. Do `mvn spring-boot:run` in a terminal with the client repository as the working directory
2. Open a browser and go to http://localhost:`port` based on the `port` that the application is using, which given in a message like `Tomcat started on port 8081 (http) with context path '/'` in the terminal
### User authentication/registration
1. Click on `Don't have an account? Register` 
2. Make up a username `name` and password `pw` and register an account under these credentials
3. Check in the Firestore console that an entry was saved under `name` and `pw`, with a corresponding `accountId`
4. Log in using the credentials from step 2
5. The application should respond with a JavaScript alert `Log-in successful!`
6. Click on `OK`, and you should be redirected to the home page
### Searching for nearby listings and making a request
1. Enter `nyc` and select the first drop down from the search bar
2. Click on `Search`
3. You should see 2 listings, `Fruit Basket` and `Cereal`
5. Click on `Select` for `Cereal`, and enter `1`.
6. You should see `Successfully made the food request.`
7. Check that a new entry was added to the `FoodRequest` table in the service database, and check that the foreign key `listingId` of this entry is the `listingId` for `Cereal`, 14.
